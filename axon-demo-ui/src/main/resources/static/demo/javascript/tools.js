var Tools = {};
(function($) {
    Tools.init = function() {
        console.log('Init Tools Module')
        Tools.modal.init();
    };
    Tools.setTitle = function(title) {
        $('h2#title').html(_.ucFirst(title));
    };
    Tools.formToJson = function($form) {
        return  $form.serializeJSON();
    };
    Tools.jsonToForm = function($form, json) {
        for (var i in json) {
            var value = json[i];
            if (_.isArray(value)) {
                var $field = $form.find('[name^="'+i+'[]"]').val(value);
                var name = $field.attr('name');

                var properties = name.replace('[]', '').match(/\[.+?\]/g) || [];
                properties = properties.map(function(str) {
                    return str.slice(1,-1)
                });

                var idProperty = properties.pop();

                if (properties.length > 0) {
                    _.each(properties, function(property) {
                        value = _.pluck(value, property);
                    });
                }

                $field.val(_.pluck(value, idProperty)).trigger('change');

            } else {
                var $field = $form.find('[name="'+i+'"]').val(value);
                if ($field.is('select')) {
                    $field.val(value).trigger('change');
                } else {
                    $field.val(value);
                }
            }
        }
    };

    Tools.modal = {};

    Tools.modal.init = function() {
        this.modalTmpl = Handlebars.compile($('#modal-tmpl').html());
    };

    Tools.modal.show = function(options) {
        var self = this;

        this.data = {
            title :                 options.title,
            content :               options.content,
            confirmLbl :            !_.isUndefined(options.confirmLbl) ? options.confirmLbl : 'Confirm',
            cancelLbl :             !_.isUndefined(options.cancelLbl) ? options.cancelLbl : 'Cancel',
            size :                  _.isUndefined(options.size) ? '' : options.size,
            showConfirmButton :     _.isUndefined(options.showConfirmButton) ? true : options.showConfirmButton,
            showCancelButton :      _.isUndefined(options.showCancelButton) ? true : options.showCancelButton,
            showDismissButton :     _.isUndefined(options.showDismissButton) ? true : options.showDismissButton,
            ready :                 !_.isUndefined(options.ready) ? options.ready : function() {},
            callback :              !_.isUndefined(options.callback) ? options.callback : function() {},
            cancel :                !_.isUndefined(options.cancel) ? options.cancel : function() {},
            close :                 !_.isUndefined(options.close) ? options.close : function() {}
        };
        this.modal = $(self.modalTmpl(this.data));

        this.open = function () {
            self.modal.modal(self.options);

            if (_.isFunction(self.data.ready)) {
                self.data.ready(self);
            }
        };

        this.init = function() {
            $('#myModal').remove();
            $('body').append(this.modal); //put modal in document to be able to register events
            self.open();
            self.registerButtons();
        };

        this.registerButtons = function() {
            self.$confirmBtn = self.modal.find('.modal-footer button.btn-primary');
            self.$confirmBtn.click(function(evt) {
                evt.preventDefault();
                var value = self.data.callback(self);
                if (options.autohide) {
                    self.close();
                }
            });
            self.$cancelBtn = self.modal.find('.modal-footer button.btn-default');
            self.$cancelBtn.click(function(evt) {
                if (_.isFunction(self.data.cancel)) {
                    self.data.cancel(self);
                }
            });
            self.modal.on('hidden.bs.modal', function () {
                self.data.close(self);
            })
        };

        this.close = function() {
            self.modal.modal('hide');
        };

        this.init();
    };

    _.mixin({
        isBlank: function(string) {
            return (_.isUndefined(string) || _.isNull(string) || (_.isString(string) && string.trim().length === 0))
        },
        guid : function(){
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                var r = Math.random()*16|0, v = c === 'x' ? r : (r&0x3|0x8);
                return v.toString(16);
            });
        },
        ucFirst : function(value) {
            return value.charAt(0).toUpperCase() + value.slice(1);
        }
    });

})(jQuery);