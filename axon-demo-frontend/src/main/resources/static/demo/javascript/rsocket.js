var RSocket = {};
(function($) {
    RSocket.init = function() {
        var self = this;
    };

    RSocket.addNewStream = function(settings) {
        // blabla, Let's use the RSocket var to pull rsocket stuff into this module
        // Create an instance of a client
        var client = new this.RSocketClient({
            serializers: {
                data: this.JsonSerializer,
                metadata: this.IdentitySerializer
            },
            setup: {
                // ms btw sending keepalive to server
                keepAlive: 60000,
                // ms timeout if no keepalive response
                lifetime: 180000,
                // format of `data`
                dataMimeType: 'application/json',
                // format of `metadata`
                metadataMimeType: 'message/x.rsocket.routing.v0',
            },
            transport: new this.RSocketWebSocketClient({
                url: 'wss://axon-demo-accounts-kristof86.cloud.okteto.net/rsocket'
            }),
        });

        // Open the connection
        client.connect().subscribe({
            onComplete: socket => {
                // socket provides the rsocket interactions fire/forget, request/response,
                // request/stream, etc as well as methods to close the socket.
                socket.requestStream({
                    data: settings.data,
                    metadata: String.fromCharCode(settings.route.length) + settings.route,
                }).subscribe({
                    onComplete: () => console.log('complete'),
                    onError: error => {
                        console.log(error);
                    },
                    onNext: payload => {
                        console.log(payload.data);
                        settings.onNext(payload.data);
                    },
                    onSubscribe: subscription => {
                        console.log(subscription)
                        subscription.request(2147483647);
                    }
                });
            },
            onError: error => {
                console.log(error);
            },
            onSubscribe: cancel => {
                /* call cancel() to abort */
            }
        });
    }
})(jQuery);