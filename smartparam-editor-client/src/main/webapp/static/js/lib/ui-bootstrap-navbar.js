
var module = angular.module('ui.bootstrap.navbar', []);

module.controller('NavbarController', ['$scope', '$element', '$location', function($scope, $element, $location) {
    var navItems = $scope.navItems = [];

    this.select = $scope.select = function selectTab(navItem) {
        angular.forEach(navItems, function(navItem) {
            navItem.selected = false;
        });
        navItem.selected = true;
    };

    $scope.clear = function clearSelection() {
        angular.forEach(navItems, function(navItem) {
            navItem.selected = false;
            if(navItem.location === '#') {
                navItem.selected = true;
            }
        });
    };

    this.addNavItem = function(navItem) {
        var currentLocation = $location.path();
        if(currentLocation.length > 0 && currentLocation.indexOf(navItem.location) >= 0) {
            $scope.select(navItem);
        }
        navItems.push(navItem);
    };

    this.removeNavItem = function removeNavItem(navItem) {
        var index = navItems.indexOf(navItem);
        navItems.splice(index, 1);

        if (navItem.selected && navItems.length > 0) {
            $scope.select(navItems[index < navItems.length ? index : index-1]);
        }
    };
}]);

module.directive('navbar', function() {
    return {
        restrict: 'E',
        replace: true,
        transclude: true,
        scope: {
            header: '@'
        },
        controller: 'NavbarController',
        template: '<div class="navbar">' +
                    '<div class="navbar-inner">' +
                        '<a class="brand" href="#" ng-click="clear()">{{header}}</a>' +
                        '<ul class="nav" ng-transclude>' +
                        '</ul>' +
                    '</div>' +
                '</div>'
    };
})
.directive('navitem', ['$parse', function($parse) {
    return {
        restrict: 'E',
        require: '^navbar',
        replace: true,
        transclude: true,
        scope: {
            location: '@'
        },
        template: '<li ng-class="{active: selected}">' +
                    '<a ng-href="{{location}}" ng-click="select()" ng-transclude></a>' +
                '</li>',
        link: function(scope, element, attrs, controller) {
            var getSelected, setSelected;
            scope.selected = false;
            scope.navItem = scope;
            scope.location = attrs.location;

            scope.select = function() {
                controller.select(scope);
            };

            if(attrs.active) {
                getSelected = $parse(attrs.active);
                setSelected = getSelected.assign;
                scope.$watch(
                    function watchSelected() {return getSelected(scope.$parent);},
                    function updateSelected(value) {scope.selected = value;}
                );
                scope.selected = getSelected ? getSelected(scope.$parent) : false;
            }

            scope.$watch('selected', function(selected) {
                if(selected) {
                    controller.select(scope);
                }
                if(setSelected) {
                    setSelected(scope.$parent, selected);
                }
            });

            controller.addNavItem(scope);
            scope.$on('$destroy', function() {
                controller.removeNavItem(scope);
            });
        }
    };
}]);
