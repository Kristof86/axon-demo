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
            this.page = 'OVERVIEW';
            Tools.setTitle('Accounts')
            this.overview();
        }.bind(this);

        const accountDetailHandler = function(id) {
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

        let accounts;
        if (typeof (accounts) === 'undefined' ) {
            App.$container.html(self.accountsOverviewTmpl(
                { accounts : [] }
            ));
        }

        Websockets.connect

        var socket = new SockJS(App.registry.accountsServiceRestUrl + "/accounts");
        const stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            //setConnected(true);
            console.log('Connected: ' + frame);
            stompClient.subscribe( '/topic/accounts/all', function (accounts) {
                console.log('accounts', accounts);
                accounts = JSON.parse(accounts.body);
                if (self.page === 'OVERVIEW') {
                    App.$container.html(self.accountsOverviewTmpl(
                        { accounts : accounts }
                    ));
                }
            });
            stompClient.send("/app/accounts/all", {}, {});
        });


/*
        function disconnect() {
            if (stompClient !== null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
        }


        RSocket.addNewStream({
            route : 'accounts.all',
            onNext: function(data) {
                accounts = data;
                if (self.page === 'OVERVIEW') {
                    App.$container.html(self.accountsOverviewTmpl(
                        { accounts : accounts }
                    ));
                }
                $('.delete-account').click(function(evt) {
                    evt.preventDefault();
                    const id = $(this).data('id');
                    $.ajax({
                        type: 'DELETE',
                        dataType: 'json',
                        contentType : 'application/json',
                        url: App.registry.accountsServiceRestUrl + '/deleteAccount/' + id,
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
                                url: App.registry.accountsServiceRestUrl + '/changeName/' + id,
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
        })
        */


        const addAccountHandler = this.addAccount.bind(this);
        $(document).on('click','body #add-account', addAccountHandler);
    };

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
                    url: App.registry.accountsServiceRestUrl + '/createAccount',
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
        Websockets.addNewStream({
            route : 'accounts.detail',
            data : {id : id},
            onNext: function(data) {
                account = data;
                if (self.page === 'DETAILS') {
                    App.$container.html(self.accountDetailTmpl(
                        { account : data }
                    ));
                }
            }
        })
    };


})(jQuery);