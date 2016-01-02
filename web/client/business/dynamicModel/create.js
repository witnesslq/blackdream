define(
    ["business/module", "business/api"],
    function (module) {
        "use strict";

        module.controller("dynamicModelCreateController", [
            "$scope", "$routeParams", "location", "dynamicModelApi",
            function($scope, $routeParams, location, dynamicModelApi){
                var generatorId = $routeParams.generatorId;

                $scope.createRequest = {generatorId: generatorId, children:[],properties:[],association:[]};

                $scope.childrenControl = {};
                dynamicModelApi.query({generatorId:generatorId}).success(function(children){
                    $scope.childrenControl.children = children;
                });

                $scope.create = function(){
                    for(var i = 0 ; i < $scope.childrenControl.children.length ; i++){
                        var child = $scope.childrenControl.children[i];
                        if(child.$checked){
                            $scope.createRequest.children.push(child.id);
                        }
                    }
                    dynamicModelApi.create($scope.createRequest).success(function(){
                        location.go("/business/dynamicModel/manage/" + generatorId);
                    });
                };

                $scope.iconControl = {
                    $error: {},
                    icons:[
                        "asterisk", "plus", "euro", "eur", "minus", "cloud", "envelope", "pencil", "glass", "music", "search", "heart", "star", "star-empty", "user", "film",
                        "th-large", "th", "th-list", "ok", "remove", "zoom-in", "zoom-out", "off", "signal", "cog", "trash", "home", "file", "time", "road", "download-alt",
                        "download", "upload", "inbox", "play-circle", "repeat", "refresh", "list-alt", "lock", "flag", "headphones", "volume-off", "volume-down", "volume-up", "qrcode", "barcode", "tag",
                        "tags", "book", "bookmark", "print", "camera", "font", "bold", "italic", "text-height", "text-width", "align-left", "align-center", "align-right", "align-justify", "list", "indent-left",
                        "indent-right", "facetime-video", "picture", "map-marker", "adjust", "tint", "edit", "share", "check", "move", "step-backward", "fast-backward", "backward", "play", "pause", "stop",
                        "forward", "fast-forward", "step-forward", "eject", "chevron-left", "chevron-right", "plus-sign", "minus-sign", "remove-sign", "ok-sign", "question-sign", "info-sign", "screenshot", "remove-circle", "ok-circle", "ban-circle",
                        "arrow-left", "arrow-right", "arrow-up", "arrow-down", "share-alt", "resize-full", "resize-small", "exclamation-sign", "gift", "leaf", "fire", "eye-open", "eye-close", "warning-sign", "plane",
                        "calendar", "random", "comment", "magnet", "chevron-up", "chevron-down", "retweet", "shopping-cart", "folder-close", "folder-open", "resize-vertical", "resize-horizontal", "hdd", "bullhorn",
                        "bell", "certificate", "thumbs-up", "thumbs-down", "hand-right", "hand-left", "hand-up", "hand-down", "circle-arrow-right", "circle-arrow-left", "circle-arrow-up", "circle-arrow-down", "globe",
                        "wrench", "tasks", "filter", "briefcase", "fullscreen", "dashboard", "paperclip", "heart-empty", "link", "phone", "pushpin", "usd", "gbp", "sort", "sort-by-alphabet", "sort-by-alphabet-alt",
                        "sort-by-order", "sort-by-order-alt", "sort-by-attributes", "sort-by-attributes-alt", "unchecked", "expand", "collapse-down", "collapse-up", "log-in", "flash", "log-out", "new-window",
                        "record", "save", "open", "saved", "import", "export", "send", "floppy-disk", "floppy-saved", "floppy-remove", "floppy-save", "floppy-open", "credit-card", "transfer", "cutlery",
                        "header", "compressed", "earphone", "phone-alt", "tower", "stats", "sd-video", "hd-video", "subtitles", "sound-stereo", "sound-dolby", "sound-5-1", "sound-6-1", "sound-7-1", "copyright-mark",
                        "registration-mark", "cloud-download", "cloud-upload", "tree-conifer", "tree-deciduous", "cd", "save-file", "open-file", "level-up", "copy", "paste", "alert", "equalizer", "king", "queen",
                        "pawn", "bishop", "knight", "baby-formula", "tent", "blackboard", "bed", "apple", "erase", "hourglass", "lamp", "duplicate", "piggy-bank", "scissors", "bitcoin", "btc", "xbt", "yen",
                        "jpy", "ruble", "rub", "scale", "ice-lolly", "ice-lolly-tasted", "education", "option-horizontal", "option-vertical", "menu-hamburger", "modal-window", "oil", "grain", "sunglasses", "text-size",
                        "text-color", "text-background", "object-align-top", "object-align-bottom", "object-align-horizontal", "object-align-left", "object-align-vertical", "object-align-right", "triangle-right", "triangle-left",
                        "triangle-bottom", "triangle-top", "console", "superscript", "subscript", "menu-left", "menu-right", "menu-down", "menu-up"
                    ],
                    selectedItem: null,
                    clear: function(){
                        $scope.iconControl.selectedItem = null;
                        $scope.iconControl.$error = {};
                    },
                    select: function(icon){
                        $scope.iconControl.clear();
                        $scope.createRequest.icon = icon;
                        $scope.iconControl.selectedItem = icon;
                        $scope.iconControl.$error = null;
                    }
                };

                $scope.hashKey = function(entity){
                    return entity.$$hashKey.split(":")[1];
                };

                $scope.validateMessages = {
                    name:{
                        required:"必输项",
                        maxlength:"最长20位"
                    }
                };

                $scope.getMessage = function(field, $error, validateMessages){
                    for(var k in $error)
                        return validateMessages[field][k];
                };

                $scope.propertyControl = {
                    typeOptions:[
                        { type: "Boolean" },
                        { type: "Long" },
                        { type: "Double" },
                        { type: "String" },
                        { type: "Date" },
                        { type: "Enum" },
                        { type: "Model" }
                    ],
                    validatorRules:{
                        Boolean:{required:true},
                        Long:{required:true,min:true,max:true,minlength:true,maxlength:true,pattern:true},
                        Double:{required:true,min:true,max:true,minlength:true,maxlength:true,pattern:true},
                        String:{required:true,minlength:true,maxlength:true,pattern:true},
                        Date:{required:true},
                        Enum:{required:true},
                        Model:{required:true}
                    },
                    addOptionalValue:function(property, $event){
                        if($event.keyCode == 13){
                            if(property.optionalValue){
                                if(!property.optionalValues){
                                    property.optionalValues = [];
                                }
                                property.optionalValues.push(property.optionalValue);
                                property.optionalValue = null;
                            }
                        }
                    },
                    removeOptionalValue: function(property, $index){
                        property.optionalValues.splice($index, 1);
                    },
                    changeType:function(property){
                        property.defaultValue = null;
                        property.optionalValues = null;
                        property.validator = {};
                    },
                    validateMessages: {
                        name:{
                            required:"必输项",
                            pattern:"非数字开头20位长度字母或数字"
                        },
                        label:{
                            required:"必输项",
                            maxlength:"最长10位"
                        },
                        type:{
                            required:"必输项"
                        },
                        viewWidth:{
                            required:"必输项",
                            min:"最小0",
                            max:"最大1000"
                        }
                    }
                };

                $scope.propertiesControl = {
                    sortableOptions:{
                        update: function(e, ui) {
                        },
                        stop: function(e, ui) {
                        }
                    },
                    add:function() {
                        $scope.createRequest.properties.push({});
                    },
                    delete:function(entity, index) {
                        $scope.createRequest.properties.splice(index, 1);
                    }
                };

                $scope.associationControl = {
                    sortableOptions:{
                        update: function(e, ui) {
                        },
                        stop: function(e, ui) {
                        }
                    },
                    add:function() {
                        $scope.createRequest.association.push({});
                    },
                    delete:function(entity, index) {
                        $scope.createRequest.association.splice(index, 1);
                    }
                };

            }
        ]);
    }
);
