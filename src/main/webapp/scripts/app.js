angular
  .module('rlmsApp', [
    'theme',
    'theme.demos',
    'base64'
  ])
  .config(['$provide', '$routeProvider', function($provide, $routeProvider) {
    'use strict';
    $routeProvider
      .when('/', {
        templateUrl: 'views/company-management.html',
        resolve: {
          loadCalendar: ['$ocLazyLoad', function($ocLazyLoad) {
            return $ocLazyLoad.load([
              'bower_components/fullcalendar/fullcalendar.js',
            ]);
          }]
        }
      })
      .when('/:templateFile', {
        templateUrl: function(param) {
          return 'views/' + param.templateFile + '.html';
        }
      })
      .when('#', {
        templateUrl: 'views/add-company.html',
      })
      .otherwise({
        redirectTo: '#/'
      });
  }])
  .run(['$http','$rootScope',function($http,$rootScope) {
	  $http({
		  method: 'POST',
		  url: '/RLMS/getLoggedInUser'
		}).then(function successCallback(response) {
		    console.log(response);
		    $rootScope.loggedInUserInfo = response;
		  }, function errorCallback(response) {
		  });
  }]);;