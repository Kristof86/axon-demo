var Accounts = {};
(function($) {
    Accounts.init = function() {
        console.log('Init Accounts Module')

        // Init templates
        this.accountsOverviewTmpl = Handlebars.compile($('#accounts-overview-tmpl').html());
        this.addAccountTmpl = Handlebars.compile($('#add-account-tmpl').html());
        this.accountDetailTmpl = Handlebars.compile($('#account-detail-tmpl').html());
        this.changeAccountNameTmpl = Handlebars.compile($('#change-account-name-tmpl').html());

        const accountOverviewHandler = function() {
            Websockets.disconnect();
            this.page = 'OVERVIEW';
            Tools.setTitle('Accounts')
            this.overview();
        }.bind(this);

        const accountDetailHandler = function(id) {
            Websockets.disconnect();
            this.page = 'DETAILS';
            Tools.setTitle('Account Details')
            this.view(id);
        }.bind(this);

        const routes = {
            '/accounts': accountOverviewHandler,
            '/accounts/:id': accountDetailHandler
        };
        // Load /accounts by default
        Router(routes).init('/accounts');
    };

    Accounts.overview = function() {
        var self = this;

        $.ajax({
            type: 'GET',
            dataType: 'json',
            contentType : 'application/json',
            url: App.registry.accountsQueryServiceRestUrl,
            success: function (accounts) {
                App.$container.html(self.accountsOverviewTmpl(
                    { accounts : accounts }
                ));
                Accounts.registerTableEvents();

                Websockets.connect({
                    url : App.registry.accountsQueryServiceRestUrl,
                    connected: function(client) {
                        client.subscribe('/topic/accounts/all', function (accounts) {
                            console.log('accounts', accounts);
                            accounts = JSON.parse(accounts.body);
                            if (self.page === 'OVERVIEW') {
                                App.$container.html(self.accountsOverviewTmpl(
                                    { accounts : accounts }
                                ));
                            }
                            Accounts.registerTableEvents();
                        });
                    }
                });
            }
        });

        const addAccountHandler = this.addAccount.bind(this);
        $(document).on('click','body #add-account', addAccountHandler);
    };

    Accounts.registerTableEvents = function() {
        var self = this;
        $('.delete-account').click(function(evt) {
            evt.preventDefault();
            const id = $(this).data('id');
            $.ajax({
                type: 'DELETE',
                dataType: 'json',
                contentType : 'application/json',
                url: App.registry.accountsCommandServiceRestUrl + '/' + id + '/delete',
                processData: false,
                success: function (data) {
                    //alert('jroepi');
                }
            });
        });

        $('.change-name').click(function(evt) {
            evt.preventDefault();
            const id = $(this).data('id');
            const content = $(this).data('content');
            console.log(content);
            Tools.modal.show({
                title : 'Change Name',
                content : self.changeAccountNameTmpl(
                    { account: content }
                ),
                confirmLbl : 'Change Name',
                autohide : true,
                callback : function(modal) {
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        contentType : 'application/json',
                        url: App.registry.accountsCommandServiceRestUrl + '/' + id + '/changeName',
                        processData: false,
                        data : Tools.formToJson(modal.modal.find('form')),
                        success: function (data) {
                            //alert('jroepi');
                        }
                    });
                }
            });
        });
    }

    Accounts.addAccount = function() {
        Tools.modal.show({
            title : 'Add Account',
            content : this.addAccountTmpl(),
            confirmLbl : 'Add',
            autohide : true,
            callback : function(modal) {
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    contentType : 'application/json',
                    url: App.registry.accountsCommandServiceRestUrl + '/create',
                    processData: false,
                    data : Tools.formToJson(modal.modal.find('form')),
                    success: function (data) {
                        //alert('jroepi');
                    }
                });
            }
        });
    };

    Accounts.view = function(id) {
        var self = this;
        let account;

        $.ajax({
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json',
            url: App.registry.accountsQueryServiceRestUrl + '/' + id + '/details',
            success: function (account) {
                App.$container.html(self.accountDetailTmpl(
                    {account: account}
                ));
                Websockets.connect({
                    url : App.registry.accountsQueryServiceRestUrl,
                    connected: function(client) {
                        client.subscribe('/topic/accounts/' + id + '/details', function (account) {
                            console.log('account', account);
                            account = JSON.parse(account.body);
                            if (self.page === 'DETAILS') {
                                App.$container.html(self.accountDetailTmpl(
                                    {account: account}
                                ));
                            }
                        });
                    }
                })
            }
        });
    };

//client.send("/app/accounts/details", {}, JSON.stringify({"id" : id}));

})(jQuery);