var Websockets = {};
(function($) {
    Websockets.init = function() {
        console.log('Init Websockets Module')
        var self = this;
    };

    Websockets.connect = function(settings) {
        const socket = new SockJS(settings.url);
        Websockets.stompClient = Stomp.over(socket);
        Websockets.stompClient.debug = null;
        Websockets.stompClient.connect({}, function (frame) {
            console.log('Connected to : ' + settings.url);
            settings.connected(Websockets.stompClient);
        });
    };

    Websockets.disconnect = function() {
        if (!_.isBlank(Websockets.stompClient)) {
            Websockets.stompClient.disconnect();
            console.log('Disconnected');
        }
    };

})(jQuery);