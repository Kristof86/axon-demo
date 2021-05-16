var App = {
    registry : {}
};
(function($) {
    App.init = function () {

        // Load settings
        this.loadRegistry();

        // Dynamic page content
        this.$container = $('div#container #content');

        // Init modules
        Tools.init();
        Accounts.init();
        RSocket.init();

        /*
        var scopeHandler = function () {
            Scopes.activateMenu();
        };

        var contextHandler = function () {
            Contexts.activateMenu();
        };

        var routes = {
            '/scopes': scopeHandler,
            '/contexts': contextHandler
        };

        // Scopes is default page loaded
        var router = Router(routes).init('/scopes');

         */

    };

    App.loadRegistry = function() {
        var contextPath = $("meta[name='_ctx']").attr('content');
        if (!contextPath) {
            contextPath = '';
        }
        this.registry.contextPath = contextPath;
        //this.registry.accountsServiceUrl = 'http://localhost:8080'
        this.registry.accountsServiceUrl = 'https://axon-demo-accounts-kristof86.cloud.okteto.net'
    };
})(jQuery);