/// <reference path="../../tsd.d.ts" />
var cx;
(function (cx) {
    var base;
    (function (base) {
        'use strict';
        /*
         * The ApplicationCtrl is a controller which is globally defined which means that the body tag of the index.html
         * defines this controller to get access to the security functions. With this you can check on single navigation
         * links, in the html files, if the logged-in user has the correct role to see the link.
         */
        var ApplicationCtrl = (function () {
            /*
             * @ngInject
             */
            function ApplicationCtrl($state, $translate) {
                this.$state = $state;
                this.$translate = $translate;
                this.availableLanguages = ["de", "en"];
                this.currentLanguage = $translate.use(); //getter call to currently used language
                if (!this.currentLanguage) {
                    // firefox has the browser language in the proposedLanguage but not in the §translate.use()
                    this.currentLanguage = $translate.proposedLanguage();
                }
            }
            ApplicationCtrl.prototype.changeLanguage = function (languageKey) {
                this.currentLanguage = languageKey;
                this.$translate.use(languageKey);
            };
            return ApplicationCtrl;
        })();
        base.ApplicationCtrl = ApplicationCtrl;
    })(base = cx.base || (cx.base = {}));
})(cx || (cx = {}));

/// <reference path="../../tsd.d.ts" />
var cx;
(function (cx) {
    var backends;
    (function (backends) {
        'use strict';
        /* @ngInject */
        function buildBackendRestangular(Restangular) {
            return Restangular.withConfig(function (RestangularConfigurer) {
                RestangularConfigurer.setBaseUrl('/api');
            });
        }
        backends.buildBackendRestangular = buildBackendRestangular;
    })(backends = cx.backends || (cx.backends = {}));
})(cx || (cx = {}));

/// <reference path="../../tsd.d.ts" />
var cx;
(function (cx) {
    var routing;
    (function (routing) {
        'use strict';
        /*
         * This configuration part defines the routing of the application.
         * It defines the states, the urls, the templates and the controllers.
         * The controllers are defined with the 'controller as' syntax, so that you doesn't need
         * to inject a $scope variable and can access the function and variables from the controller in
         * an object oriented way.
         */
        function applyRoutingConfig($stateProvider, $urlRouterProvider, blockUIConfig) {
            $urlRouterProvider.otherwise('/');
            $stateProvider.state("home", {
                url: "/",
                templateUrl: 'home/home-main.html'
            }).state("todos", {
                abstract: true,
                url: "/todos",
                templateUrl: 'todos/todos.html'
            }).state("todos.list", {
                url: "/list",
                controller: 'TodosCtrl as todosCtrl',
                templateUrl: '../todos/todos.list.html'
            }).state("todos.detail", {
                url: "/details/{todoId}",
                controller: "TodoDetailsCtrl as todoDetailsCtrl",
                templateUrl: "../todos/todos.detail.html"
            }).state("todos.new", {
                url: "/new",
                controller: "TodoCreationCtrl as todoCreationCtrl",
                templateUrl: "../todos/todos.form.html"
            });
            blockUIConfig.preventRouting = false;
        }
        routing.applyRoutingConfig = applyRoutingConfig;
    })(routing = cx.routing || (cx.routing = {}));
})(cx || (cx = {}));

/// <reference path="../../tsd.d.ts" />
var cx;
(function (cx) {
    var translation;
    (function (translation) {
        'use strict';
        /*
         * Configuration for internationalization. Tells the translateProvider where to search for the translations and to
         * determine the preferred language of the client.
         *
         * @ngInject
         */
        function configureTranslateProvider($translateProvider) {
            $translateProvider.useStaticFilesLoader({
                prefix: 'i18n/',
                suffix: '.json'
            }).fallbackLanguage('en').registerAvailableLanguageKeys(['en', 'de'], {
                'en*': 'en',
                'de*': 'de'
            }).determinePreferredLanguage().useLocalStorage();
        }
        translation.configureTranslateProvider = configureTranslateProvider;
    })(translation = cx.translation || (cx.translation = {}));
})(cx || (cx = {}));

/// <reference path="../../tsd.d.ts" />
var cx;
(function (cx) {
    var todos;
    (function (todos) {
        var TodosService = (function () {
            /** @ngInject */
            function TodosService(CxRestangular) {
                this.CxRestangular = CxRestangular;
            }
            TodosService.prototype.buildSortString = function (sorting) {
                var result = "";
                for (var key in sorting) {
                    result += key + "," + sorting[key];
                    break;
                }
                return result;
            };
            TodosService.prototype.buildQuery = function (params) {
                console.log(params);
                console.log(params.filter());
                console.log(params.sorting());
                var filter = params.filter();
                console.log(this.buildSortString(params.sorting()));
                return {
                    title: filter.title ? filter.title : "",
                    tags: filter.tags ? filter.tags : [],
                    page: params.page() - 1,
                    size: params.count(),
                    sort: this.buildSortString(params.sorting())
                };
            };
            TodosService.prototype.convertToTodoItem = function (item) {
                var res = {
                    id: item.id,
                    title: item.title,
                    tags: item.tags.map(function (tag) {
                        return { text: tag };
                    }),
                    createdDate: item.createdDate ? new Date(item.createdDate) : null,
                    dueDate: item.dueDate ? new Date(item.dueDate) : null
                };
                return res;
            };
            TodosService.prototype.convertToRestTodoItem = function (item) {
                console.log(item);
                var res = {
                    id: item.id,
                    title: item.title,
                    tags: item.tags.map(function (tag) { return tag.text; }),
                    createdDate: item.createdDate ? item.createdDate.valueOf() : null,
                    dueDate: item.dueDate ? item.dueDate.valueOf() : null
                };
                console.log(res);
                return res;
            };
            TodosService.prototype.convertToTodoItems = function (items) {
                var _this = this;
                return items.map(function (item) { return _this.convertToTodoItem(item); });
            };
            TodosService.prototype.convertToRestTodoItems = function (items) {
                var _this = this;
                return items.map(function (item) { return _this.convertToRestTodoItem(item); });
            };
            TodosService.prototype.getAllTodos = function (params) {
                var _this = this;
                var query = this.buildQuery(params);
                return this.CxRestangular.all("todos").customGET("", query).then(function (pagedTodosResponse) {
                    pagedTodosResponse.content = _this.convertToTodoItems(pagedTodosResponse.content);
                    return pagedTodosResponse;
                });
            };
            TodosService.prototype.getTodo = function (id) {
                var _this = this;
                return this.CxRestangular.one("todos", id).get().then(function (restItem) { return _this.convertToTodoItem(restItem); });
            };
            TodosService.prototype.createTodo = function (item) {
                var restItem = this.convertToRestTodoItem(item);
                return this.CxRestangular.all("todos").customPOST(restItem);
            };
            TodosService.prototype.updateTodo = function (item) {
                var restItem = this.convertToRestTodoItem(item);
                return this.CxRestangular.one("todos", restItem.id).customPUT(restItem);
            };
            TodosService.prototype.deleteTodo = function (itemId) {
                return this.CxRestangular.one("todos", itemId).remove();
            };
            return TodosService;
        })();
        todos.TodosService = TodosService;
        var TodosCtrl = (function () {
            function TodosCtrl(TodosService, ngTableParams, $state) {
                this.TodosService = TodosService;
                this.ngTableParams = ngTableParams;
                this.$state = $state;
                this.formatTags = function (tags) {
                    return tags.map(function (tag) { return tag.text; }).join(', ');
                };
                this.tableParams = new this.ngTableParams({
                    page: 1,
                    count: 10,
                    sorting: {
                        id: 'asc'
                    }
                }, {
                    counts: [],
                    getData: this.loadItems.bind(this)
                });
            }
            TodosCtrl.prototype.loadItems = function ($defer, params) {
                this.TodosService.getAllTodos(params).then(function (pagedTodoResponse) {
                    params.total(pagedTodoResponse.totalElements);
                    $defer.resolve(pagedTodoResponse.content);
                });
            };
            TodosCtrl.prototype.gotoDetails = function (item) {
                this.$state.go('todos.detail', { todoId: item.id });
            };
            return TodosCtrl;
        })();
        todos.TodosCtrl = TodosCtrl;
        var TodoDetailsCtrl = (function () {
            function TodoDetailsCtrl(TodosService, $stateParams, $state) {
                this.TodosService = TodosService;
                this.$stateParams = $stateParams;
                this.$state = $state;
                this.dueDatePickerOpened = false;
                this.dateOptions = {
                    formatYear: 'yyyy',
                    startingDay: 1
                };
                var id = $stateParams['todoId'];
                console.log("fetching item: ", id);
                this.getDetails(id);
            }
            TodoDetailsCtrl.prototype.openDueDatePicker = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                this.dueDatePickerOpened = true;
            };
            TodoDetailsCtrl.prototype.getDetails = function (id) {
                var _this = this;
                this.TodosService.getTodo(id).then(function (item) { return _this.selectedItem = item; });
            };
            TodoDetailsCtrl.prototype.updateTodo = function (item) {
                var _this = this;
                console.log("updating item: ", item);
                this.TodosService.updateTodo(item).then(function (resp) { return _this.setSuccess(); }, function (resp) { return _this.setFailure(resp); });
            };
            TodoDetailsCtrl.prototype.deleteTodo = function (item) {
                var _this = this;
                this.TodosService.deleteTodo(item.id).then(function () { return _this.$state.go("todos.list"); }, function (resp) { return _this.setFailure(resp); });
            };
            TodoDetailsCtrl.prototype.setSuccess = function () {
                this.success = "successfully updated.";
                this.err = null;
            };
            TodoDetailsCtrl.prototype.setFailure = function (resp) {
                this.err = "error updating: " + resp.status;
                this.success = null;
            };
            return TodoDetailsCtrl;
        })();
        todos.TodoDetailsCtrl = TodoDetailsCtrl;
        var TodoCreationCtrl = (function () {
            function TodoCreationCtrl(TodosService, TagsService, $state) {
                this.TodosService = TodosService;
                this.TagsService = TagsService;
                this.$state = $state;
                this.item = {};
                this.success = null;
                this.failure = null;
                this.dueDatePickerOpened = false;
                this.dateOptions = {
                    formatYear: 'yyyy',
                    startingDay: 1
                };
            }
            TodoCreationCtrl.prototype.openDueDatePicker = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                this.dueDatePickerOpened = true;
            };
            TodoCreationCtrl.prototype.loadAutoCompleteTags = function ($query) {
                console.log($query);
                //return this.TagsService.getAllTags($query).then((suggestions) => {console.log(suggestions); return suggestions});
                return this.TagsService.getAllTags($query);
            };
            TodoCreationCtrl.prototype.createTodo = function (item) {
                var _this = this;
                console.log(item);
                this.TodosService.createTodo(item).then(function (respitem) {
                    _this.failure = null;
                    _this.success = "successfully created item";
                    _this.$state.go("todos.detail", { todoId: respitem.id });
                }, function (err) {
                    _this.success = null;
                    _this.failure = err.status;
                });
            };
            return TodoCreationCtrl;
        })();
        todos.TodoCreationCtrl = TodoCreationCtrl;
        var TagsService = (function () {
            function TagsService($timeout, $q, CxRestangular) {
                this.$timeout = $timeout;
                this.$q = $q;
                this.CxRestangular = CxRestangular;
            }
            TagsService.prototype.getAllTags = function (query) {
                console.log(query);
                var deferred = this.$q.defer();
                var res = ['freizeit', 'hochschule', 'arbeit', 'fun', 'einkaufen', 'links', 'thesis', 'doku', 'geheim', 'vortrag', 'kochen', 'backen', 'rezept', 'garten', 'tutorial'];
                var matchesCurrentQuery = function (tag) { return (tag.lastIndexOf(query, 0) == 0); };
                this.$timeout(function () {
                    console.log(res);
                    deferred.resolve(res.filter(function (tag) { return matchesCurrentQuery(tag); }));
                }, 0);
                console.log(deferred.promise);
                return deferred.promise;
            };
            return TagsService;
        })();
        todos.TagsService = TagsService;
    })(todos = cx.todos || (cx.todos = {}));
})(cx || (cx = {}));

/**
 * Created by mn on 22.05.2015.
 */
/// <reference path="../../tsd.d.ts" />
/// <reference path="./translation.ts" />
/// <reference path="./backends.ts" />
/// <reference path="./routing.ts" />
/// <reference path="./app-controller.ts" />
var cx;
(function (cx) {
    var base;
    (function (base) {
        'use strict';
        var app = angular.module('cluetec-webstack-app', ['restangular', 'ui.router', 'ngTable', 'pascalprecht.translate', 'blockUI', 'cx-todos', 'ngCookies']);
        app.config(cx.translation.configureTranslateProvider).controller('ApplicationCtrl', base.ApplicationCtrl).config(cx.routing.applyRoutingConfig).factory('CxRestangular', cx.backends.buildBackendRestangular);
    })(base = cx.base || (cx.base = {}));
})(cx || (cx = {}));

/// <reference path="../../tsd.d.ts" />
/// <reference path="./todos.ts" />
var cx;
(function (cx) {
    var todos;
    (function (todos) {
        'use strict';
        var app = angular.module('cx-todos', ['ngTagsInput', 'ui.bootstrap']).service('TodosService', todos.TodosService).service('TagsService', todos.TagsService).controller('TodosCtrl', todos.TodosCtrl).controller('TodoDetailsCtrl', todos.TodoDetailsCtrl).controller('TodoCreationCtrl', todos.TodoCreationCtrl);
    })(todos = cx.todos || (cx.todos = {}));
})(cx || (cx = {}));

//# sourceMappingURL=maps/main.js.map