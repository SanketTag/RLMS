angular
  .module('theme.core.navigation_controller', ['theme.core.services'])
  .controller('NavigationController', ['$scope', '$location', '$timeout','$rootScope', function($scope, $location, $timeout,$rootScope) {
    'use strict';
    $scope.menu = [ {
	        label: 'Dashboard',
	        iconClasses: 'glyphicon glyphicon-home',
	        url: '#/company-management'
   	      },{
   	      label: 'Admin',
   	      iconClasses: 'glyphicon glyphicon-user glyphicons-user-add',
   	      children: [{
   	          label: 'Manage Users',
   	          url: '#/user-management'
   	      },
   	      {
   	        label: 'Manage Companies',
   	        url: '#/company-management'
   	      },
   	      {
   	          label: 'Manage Branches',
   	          url: '#/branch-management'
   	      },
   	      {
   	          label: 'Assign Role',
   	          url: '#/assign-role'
   	      },
   	      {
   	          label: 'Manage Customers',
   	          url: '#/customer-management'
   	      },
   	      {
   	          label: 'Manage Member',
   	          url: '#/member-management'
   	      },
   	      {
   	    	  label:'Manage Lift',
   	    	  url: "#/lift-management"
   	      },
   	      /*{
   	    	  label:'Work List',
   	    	  url: "#/work-list"
   	      }*/
   	      ]
   	    },
   	    {
 	      label: 'Complaints',
   	      iconClasses: 'glyphicon glyphicon-user ',
   	      children: [{
   	          label: 'Manage Complaint',
   	          url: '#/complaint-management'
   	      }
   	      ]
   	    },
   	    {
   	    	label: 'AMC',
   	   	    iconClasses: 'glyphicon glyphicon-user ',
   	   	    children: [{
   	   	        label: 'Manage AMC',
   	   	        url: '#/amc-management'
   	      }]
   	    },
   	    {
   	    	label: 'Report',
   	   	    iconClasses: 'glyphicon glyphicon-user ',
   	   	    children: [{
   	   	        label: 'Site Visit Report',
   	   	        url: '#/reports'
   	      },{
   	   	        label: 'Technician Wise Report',
   	   	        url: '#/technician-wise-report'
   	      }]
   	    },
   	 {
   	    	label: 'Configure Lift',
   	   	    iconClasses: 'glyphicon glyphicon-user ',
   	   	    children: [{
   	   	        label: 'Configure Lift',
   	   	        url: '#/configure-lift'
   	      }]
   	    }
];


    var setParent = function(children, parent) {
      angular.forEach(children, function(child) {
        child.parent = parent;
        if (child.children !== undefined) {
          setParent(child.children, child);
        }
      });
    };

    $scope.findItemByUrl = function(children, url) {
      for (var i = 0, length = children.length; i < length; i++) {
        if (children[i].url && children[i].url.replace('#', '') === url) {
          return children[i];
        }
        if (children[i].children !== undefined) {
          var item = $scope.findItemByUrl(children[i].children, url);
          if (item) {
            return item;
          }
        }
      }
    };

    setParent($scope.menu, null);

    $scope.openItems = []; $scope.selectedItems = []; $scope.selectedFromNavMenu = false;

    $scope.select = function(item) {
      // close open nodes
      if (item.open) {
        item.open = false;
        return;
      }
      for (var i = $scope.openItems.length - 1; i >= 0; i--) {
        $scope.openItems[i].open = false;
      }
      $scope.openItems = [];
      var parentRef = item;
      while (parentRef !== null) {
        parentRef.open = true;
        $scope.openItems.push(parentRef);
        parentRef = parentRef.parent;
      }

      // handle leaf nodes
      if (!item.children || (item.children && item.children.length < 1)) {
        $scope.selectedFromNavMenu = true;
        for (var j = $scope.selectedItems.length - 1; j >= 0; j--) {
          $scope.selectedItems[j].selected = false;
        }
        $scope.selectedItems = [];
        parentRef = item;
        while (parentRef !== null) {
          parentRef.selected = true;
          $scope.selectedItems.push(parentRef);
          parentRef = parentRef.parent;
        }
      }
    };

    $scope.highlightedItems = [];
    var highlight = function(item) {
      var parentRef = item;
      while (parentRef !== null) {
        if (parentRef.selected) {
          parentRef = null;
          continue;
        }
        parentRef.selected = true;
        $scope.highlightedItems.push(parentRef);
        parentRef = parentRef.parent;
      }
    };

    var highlightItems = function(children, query) {
      angular.forEach(children, function(child) {
        if (child.label.toLowerCase().indexOf(query) > -1) {
          highlight(child);
        }
        if (child.children !== undefined) {
          highlightItems(child.children, query);
        }
      });
    };

    // $scope.searchQuery = '';
    $scope.$watch('searchQuery', function(newVal, oldVal) {
      var currentPath = '#' + $location.path();
      if (newVal === '') {
        for (var i = $scope.highlightedItems.length - 1; i >= 0; i--) {
          if ($scope.selectedItems.indexOf($scope.highlightedItems[i]) < 0) {
            if ($scope.highlightedItems[i] && $scope.highlightedItems[i] !== currentPath) {
              $scope.highlightedItems[i].selected = false;
            }
          }
        }
        $scope.highlightedItems = [];
      } else
      if (newVal !== oldVal) {
        for (var j = $scope.highlightedItems.length - 1; j >= 0; j--) {
          if ($scope.selectedItems.indexOf($scope.highlightedItems[j]) < 0) {
            $scope.highlightedItems[j].selected = false;
          }
        }
        $scope.highlightedItems = [];
        highlightItems($scope.menu, newVal.toLowerCase());
      }
    });

    $scope.$on('$routeChangeSuccess', function() {
      if ($scope.selectedFromNavMenu === false) {
        var item = $scope.findItemByUrl($scope.menu, $location.path());
        if (item) {
          $timeout(function() {
            $scope.select(item);
          });
        }
      }
      $scope.selectedFromNavMenu = false;
      $scope.searchQuery = '';
    });
  }]);