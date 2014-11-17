var coffeeApp = angular.module('coffeeApp', ['ngResource', 'ui.bootstrap']);


//Shortcut to create code: mgmfa
//$resource requires ngResource module above.
coffeeApp.factory('CoffeeOrder', function ($resource) {
    return $resource('/service/coffeeshop/:id/order/', {id: '@coffeeShopId'} , {});
});

coffeeApp.controller('DrinksController', function ($scope, CoffeeOrder) {
    $scope.types = [
        {name: 'Americano', family: "Coffee"},
        {name: 'Latte', family: "Coffee"},
        {name: 'Tea', family: "Other"},
        {name: 'Cappuccino', family: "Coffee"}
    ]

    $scope.sizes = ['Small', 'Medium', 'Large']

    $scope.messages = [];

    $scope.giveMeCoffee = function () {
        CoffeeOrder.save({id: 1}, $scope.drink,
            function (order) {
                $scope.messages.push({type: 'success', msg: 'Order Sent!', orderId : order.id, coffeeShopId : 1 })
            }
        );
    };

    $scope.closeAlert = function(index) {
        $scope.messages.splice(index, 1);
    };

});