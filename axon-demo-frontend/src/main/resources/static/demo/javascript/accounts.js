var Accounts = {};
(function($) {
    Accounts.init = function() {
        console.log('init')
        this.accountsOverviewTmpl = Handlebars.compile($('#accounts-overview-tmpl').html());
        this.accountsFormTmpl = Handlebars.compile($('#accounts-form-tmpl').html());
        this.accountsDetailTmpl = Handlebars.compile($('#accounts-detail-tmpl').html());
        this.accountsChangeNameTmpl = Handlebars.compile($('#accounts-change-name-tmpl').html());

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
        Router(routes).init('/accounts');
    };


    Accounts.overview = function() {
        var self = this;

        let accounts;
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
                        url: App.registry.accountsServiceUrl + '/deleteAccount/' + id,
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
                        content : self.accountsChangeNameTmpl(
                            { account: content }
                        ),
                        confirmLbl : 'Change Name',
                        autohide : true,
                        callback : function(modal) {
                            $.ajax({
                                type: 'POST',
                                dataType: 'json',
                                contentType : 'application/json',
                                url: App.registry.accountsServiceUrl + '/changeName/' + id,
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

        const addAccountHandler = this.addAccount.bind(this);
        $(document).on('click','body #add-account', addAccountHandler);
    };

    Accounts.addAccount = function() {
        Tools.modal.show({
            title : 'Add Account',
            content : this.accountsFormTmpl(),
            confirmLbl : 'Add',
            autohide : true,
            callback : function(modal) {
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    contentType : 'application/json',
                    url: App.registry.accountsServiceUrl + '/createAccount',
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
        RSocket.addNewStream({
            route : 'accounts.detail',
            data : {id : id},
            onNext: function(data) {
                account = data;
                if (self.page === 'DETAILS') {
                    App.$container.html(self.accountsDetailTmpl(
                        { account : data }
                    ));
                }
            }
        })
    };


})(jQuery);