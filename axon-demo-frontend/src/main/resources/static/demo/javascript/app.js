var App = {
    registry : {}
};
(function($) {
    App.init = function () {

        console.log('Init application');

        // Load settings
        this.loadRegistry();

        // Dynamic page content
        this.$container = $('div#container #content');

        // Init modules
        Tools.init();
        Accounts.init();
        RSocket.init();

    };

    App.loadRegistry = function() {
        var contextPath = $("meta[name='_ctx']").attr('content');
        if (!contextPath) {
            contextPath = '';
        }
        this.registry.contextPath = contextPath;
        this.registry.accountsServiceRestUrl = $("meta[name='accountsServiceRestUrl']").attr('content');
        this.registry.accountsServiceWsUrl = $("meta[name='accountsServiceWsUrl']").attr('content');
        console.log('Registry loaded ', this.registry);
    };
})(jQuery);