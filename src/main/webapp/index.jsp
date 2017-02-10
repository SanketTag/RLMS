<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>RLMS</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="Remote Lift management system">

	<link rel="icon" type="image/png" href="favicon.png">

	<!-- prochtml:remove:dist -->
	<link href="css/styles.css" rel='stylesheet' type='text/css'> 
	<link href="css/customize.css" rel='stylesheet' type='text/css'>
	<!-- /prochtml -->

	<link href='http://fonts.googleapis.com/css?family=Roboto:300,400,500,700' rel='stylesheet' type='text/css'>     

	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries. Placeholdr.js enables the placeholder attribute -->
	<!--[if lte IE 9]>
	  <link rel="stylesheet" href="assets/css/ie8.css">
	  <script type="text/javascript" src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	  <script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/respond.js/1.1.0/respond.min.js"></script>
	  <script type="text/javascript" src="bower_components/flot/excanvas.min.js"></script>
	  <script type='text/javascript' src='assets/plugins/misc/placeholdr.js'></script>
	  <script type="text/javascript" src="assets/plugins/misc/media.match.min.js"></script>
	<![endif]-->

	<!-- The following CSS are included as plugins and can be removed if unused-->

	<!-- build:css assets/css/vendor.css -->
	<!-- bower:css -->
	<link rel="stylesheet" href="bower_components/font-awesome/css/font-awesome.css" />
	<link rel="stylesheet" href="bower_components/seiyria-bootstrap-slider/dist/css/bootstrap-slider.min.css" />
	<link rel="stylesheet" href="bower_components/angular-ui-tree/dist/angular-ui-tree.min.css" />
	<link rel="stylesheet" href="bower_components/ng-grid/ng-grid.css" />
	<link rel="stylesheet" href="bower_components/angular-xeditable/dist/css/xeditable.css" />
	<!-- <link rel="stylesheet" href="bower_components/iCheck/skins/all.css" /> -->	
	<link rel="stylesheet" href="bower_components/pnotify/pnotify.core.css" />
	<link rel="stylesheet" href="bower_components/pnotify/pnotify.buttons.css" />
	<link rel="stylesheet" href="bower_components/pnotify/pnotify.history.css" />
	<link rel="stylesheet" href="bower_components/nanoscroller/bin/css/nanoscroller.css" />
	<link rel="stylesheet" href="bower_components/textAngular/src/textAngular.css" />
	<link rel="stylesheet" href="bower_components/angular-ui-grid/ui-grid.css" />
	<link rel="stylesheet" href="bower_components/switchery/dist/switchery.css" />
	<link rel="stylesheet" href="bower_components/ng-sortable/dist/ng-sortable.css" />
	<link rel="stylesheet" href="bower_components/fullcalendar/fullcalendar.css" />
	<link rel="stylesheet" href="bower_components/angular-meditor/dist/meditor.min.css" />
	<link rel="stylesheet" href="bower_components/angular-ui-select/dist/select.css" />
	<link rel="stylesheet" href="bower_components/animate.css/animate.css" />
	<link rel="stylesheet" href="bower_components/bootstrap-daterangepicker/daterangepicker-bs3.css" />
	<link rel="stylesheet" href="bower_components/nvd3/src/nv.d3.css" />
	<link rel="stylesheet" href="bower_components/skylo/vendor/styles/skylo.css" />
	<link rel="stylesheet" href="bower_components/bootstrap-datepaginator/dist/bootstrap-datepaginator.min.css" />
	<!-- endbower -->
	<link rel='stylesheet' type='text/css' href='assets/fonts/glyphicons/css/glyphicons.min.css' /> 
	<link rel='stylesheet' type='text/css' href='assets/plugins/form-fseditor/fseditor.css' /> 
	<link rel='stylesheet' type='text/css' href='assets/plugins/jcrop/css/jquery.Jcrop.min.css' /> 
	<!-- endbuild -->

	<!-- build:css({.tmp,app}) assets/css/main.css -->
	  <link rel="stylesheet" href="assets/css/styles.css">
	<!-- endbuild -->

	<!-- prochtml:remove:dist -->
	<script type="text/javascript">less = { env: 'development'};</script>
	<script type="text/javascript" src="assets/plugins/misc/less.js"></script>
	<!-- /prochtml -->
</head>

<body ng-app="rlmsApp" ng-controller="MainController"
	class="navbar-midnightblue rlms-app sidebar-default"
	class="{{getLayoutOption('sidebarThemeClass')}} {{getLayoutOption('topNavThemeClass')}}"
	ng-class="{
			  'static-header': !getLayoutOption('fixedHeader'),
			  'focusedform': getLayoutOption('fullscreen'),
			  'layout-horizontal': getLayoutOption('layoutHorizontal'),
			  'fixed-layout': getLayoutOption('layoutBoxed'),
			  'sidebar-collapsed': getLayoutOption('leftbarCollapsed') && !getLayoutOption('leftbarShown'),
			  'show-infobar': getLayoutOption('rightbarCollapsed'),
			  'show-sidebar': getLayoutOption('leftbarShown')
			}"
	ng-click="hideHeaderBar();hideChatBox()" animate-page-content
	faux-offcanvas to-top-on-load>


	<!-- 	<div ng-include="'views/templates/custom-styles.html'"></div>
 -->
	<ng-include src="'views/layout/header.html'"></ng-include>

	<!-- <nav id="headernav" class="navbar ng-hide" role="navigation" ng-show="getLayoutOption('layoutHorizontal') && !layoutLoading">
		<div class="nav">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
				<i class="fa fa-reorder"></i>
			</button>
		</div>
		<div class="collapse navbar-collapse navbar-ex1-collapse" ng-class="{'large-icons-nav': getLayoutOption('layoutHorizontalLargeIcons')}" id="horizontal-navbar">
			  <ul ng-controller="NavigationController" id="top-nav" class="nav navbar-nav">
				  <li ng-repeat="item in menu"
					  ng-if="!item.hideOnHorizontal"
					  ng-class="{ hasChild: (item.children!==undefined),
									active: item.selected,
									  open: (item.children!==undefined) && item.open,
						   'nav-separator': item.separator==true }"
					  ng-include="'templates/nav_renderer_horizontal.html'"
					></li>
			  </ul>
		</div>
	</nav>
 -->

	<div id="wrapper">
		<div id="layout-static">
			<div class="static-sidebar-wrapper" ng-show="!layoutLoading">
				<nav class="static-sidebar" role="navigation">
					<ul ng-controller="NavigationController" id="sidebar"
						sticky-scroll="50">
						<!-- <li id="search" ng-cloak>
		                <a href="">
		                	<i class="fa fa-binoculars opacity-control"></i>
		                </a>
						<form
						ng-click="$event.stopPropagation()"
						ng-submit="goToSearch()">
							<input type="text" ng-model="searchQuery" class="search-query" placeholder="Type to filter..." />
							<button type="submit" ng-click="goToSearch()"><i class="fa fa-binoculars"></i></button>
						</form>
		              </li> -->
						<li ng-repeat="item in menu"
							ng-class="{ hasChild: (item.children!==undefined),
										active: item.selected,
										  open: (item.children!==undefined) && item.open,
							   'nav-separator': item.separator==true,
		            			'search-focus': (searchQuery.length>0 && item.selected) }"
							ng-show="!(searchQuery.length && !item.selected)"
							ng-include="'templates/nav_renderer.html'"></li>
					</ul>
				</nav>
				<!-- #sidebar-->
			</div>
			<div class="static-content-wrapper">
				<div class="static-content">
					<div id="wrap" ng-view="" class="mainview-animation animated">
					</div>
					<!--wrap -->
				</div>
				
			</div>
		</div>
		<footer role="contentinfo" ng-show="!layoutLoading" ng-cloak>
					<div class="clearfix">
						<ul class="list-unstyled list-inline pull-left">
							<li>Telesist &copy; 2017</li>
						</ul>
						<button class="pull-right btn btn-default btn-sm hidden-print"
							back-to-top style="padding: 1px 10px;">
							<i class="fa fa-angle-up"></i>
						</button>
					</div>
				</footer>
	</div>

	<!-- 	<div ng-include="'views/layout/infobar.html'" class="infobar-wrapper"></div>
 -->
	<!--[if lt IE 9]>
	<script src="bower_components/es5-shim/es5-shim.js"></script>
	<script src="bower_components/json3/lib/json3.min.js"></script>
	<![endif]-->

	<script type='text/javascript'
		src='http://maps.google.com/maps/api/js?sensor=true'></script>

	<!-- build:js scripts/vendor.js -->
	<!-- bower:js -->
	<script src="bower_components/modernizr/modernizr.js"></script>
	<script src="bower_components/jquery/dist/jquery.js"></script>
	<script src="bower_components/underscore/underscore.js"></script>
	<script src="bower_components/angular/angular.js"></script>
	<script src="bower_components/angular-resource/angular-resource.js"></script>
	<script src="bower_components/angular-cookies/angular-cookies.js"></script>
	<script src="bower_components/angular-sanitize/angular-sanitize.js"></script>
	<script src="bower_components/angular-route/angular-route.js"></script>
	<script src="bower_components/angular-animate/angular-animate.js"></script>
	<script src="bower_components/bootstrap/dist/js/bootstrap.js"></script>
	<script
		src="bower_components/seiyria-bootstrap-slider/js/bootstrap-slider.js"></script>
	<script src="bower_components/angular-bootstrap/ui-bootstrap-tpls.js"></script>
	<script src="bower_components/jquery.ui/ui/jquery.ui.core.js"></script>
	<script src="bower_components/jquery.ui/ui/jquery.ui.widget.js"></script>
	<script src="bower_components/jquery.ui/ui/jquery.ui.mouse.js"></script>
	<script src="bower_components/jquery.ui/ui/jquery.ui.draggable.js"></script>
	<script src="bower_components/jquery.ui/ui/jquery.ui.resizable.js"></script>
	<script src="bower_components/jquery.easing/js/jquery.easing.js"></script>
	<script src="bower_components/flot/jquery.flot.js"></script>
	<script src="bower_components/flot/jquery.flot.stack.js"></script>
	<script src="bower_components/flot/jquery.flot.pie.js"></script>
	<script src="bower_components/flot/jquery.flot.resize.js"></script>
	<script src="bower_components/flot.tooltip/js/jquery.flot.tooltip.js"></script>
	<script src="bower_components/angular-ui-tree/dist/angular-ui-tree.js"></script>
	<script src="bower_components/moment/moment.js"></script>
	<script src="bower_components/jqvmap/jqvmap/jquery.vmap.js"></script>
	<script src="bower_components/jqvmap/jqvmap/maps/jquery.vmap.world.js"></script>
	<script
		src="bower_components/jqvmap/jqvmap/data/jquery.vmap.sampledata.js"></script>
	<script src="bower_components/ng-grid/build/ng-grid.js"></script>
	<script src="bower_components/angular-xeditable/dist/js/xeditable.js"></script>
	<!-- <script src="bower_components/iCheck/icheck.min.js"></script> -->
	<script src="bower_components/google-code-prettify/src/prettify.js"></script>
	<script src="bower_components/bootbox.js/bootbox.js"></script>
	<script src="bower_components/jquery-autosize/jquery.autosize.js"></script>
	<script src="bower_components/gmaps/gmaps.js"></script>
	<script src="bower_components/jquery.pulsate/jquery.pulsate.js"></script>
	<script src="bower_components/jquery.knob/js/jquery.knob.js"></script>
	<script src="bower_components/jquery.sparkline/index.js"></script>
	<script src="bower_components/flow.js/dist/flow.js"></script>
	<script src="bower_components/ng-flow/dist/ng-flow.js"></script>
	<script src="bower_components/enquire/dist/enquire.js"></script>
	<script src="bower_components/shufflejs/dist/jquery.shuffle.js"></script>
	<script src="bower_components/pnotify/pnotify.core.js"></script>
	<script src="bower_components/pnotify/pnotify.buttons.js"></script>
	<script src="bower_components/pnotify/pnotify.callbacks.js"></script>
	<script src="bower_components/pnotify/pnotify.confirm.js"></script>
	<script src="bower_components/pnotify/pnotify.desktop.js"></script>
	<script src="bower_components/pnotify/pnotify.history.js"></script>
	<script src="bower_components/pnotify/pnotify.nonblock.js"></script>
	<script
		src="bower_components/nanoscroller/bin/javascripts/jquery.nanoscroller.js"></script>
	<script src="bower_components/angular-nanoscroller/scrollable.js"></script>
	<script src="bower_components/rangy/rangy-core.min.js"></script>
	<script src="bower_components/rangy/rangy-cssclassapplier.min.js"></script>
	<script src="bower_components/rangy/rangy-selectionsaverestore.min.js"></script>
	<script src="bower_components/rangy/rangy-serializer.min.js"></script>
	<script src="bower_components/textAngular/src/textAngular.js"></script>
	<script src="bower_components/textAngular/src/textAngular-sanitize.js"></script>
	<script src="bower_components/textAngular/src/textAngularSetup.js"></script>
	<script src="bower_components/rangy/rangy-selectionsaverestore.js"></script>
	<script src="bower_components/angular-ui-grid/ui-grid.js"></script>
	<script src="bower_components/transitionize/dist/transitionize.js"></script>
	<script src="bower_components/fastclick/lib/fastclick.js"></script>
	<script src="bower_components/switchery/dist/switchery.js"></script>
	<script src="bower_components/ng-switchery/src/ng-switchery.js"></script>
	<script src="bower_components/ng-sortable/dist/ng-sortable.js"></script>
	<script src="bower_components/angular-meditor/dist/meditor.min.js"></script>
	<script src="bower_components/angular-ui-select/dist/select.js"></script>
	<script src="bower_components/skycons/skycons.js"></script>
	<script src="bower_components/angular-skycons/angular-skycons.js"></script>
	<script src="bower_components/jquery-validation/dist/jquery.validate.js"></script>
	<script src="bower_components/stepy/lib/jquery.stepy.js"></script>
	<script
		src="bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
	<!-- 	<script src="bower_components/d3/d3.js"></script>-->
	<!-- <script src="bower_components/nvd3/nv.d3.js"></script> -->
	<script
		src="bower_components/angularjs-nvd3-directives/dist/angularjs-nvd3-directives.js"></script>
	<script src="bower_components/oclazyload/dist/ocLazyLoad.min.js"></script>
	<script src="bower_components/skylo/vendor/scripts/skylo.js"></script>
	<script
		src="bower_components/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
	<script
		src="bower_components/jquery.easy-pie-chart/dist/angular.easypiechart.js"></script>
	<script
		src="bower_components/bootstrap-datepaginator/dist/bootstrap-datepaginator.min.js"></script>
	<script src="bower_components/velocity/velocity.js"></script>
	<script src="bower_components/velocity/velocity.ui.js"></script>
	<!-- endbower -->

	<script type='text/javascript'
		src='assets/plugins/form-colorpicker/js/bootstrap-colorpicker.min.js'></script>
	<script type='text/javascript'
		src='assets/plugins/form-fseditor/jquery.fseditor-min.js'></script>
	<script type='text/javascript'
		src='assets/plugins/form-jasnyupload/fileinput.min.js'></script>
	<script type='text/javascript'
		src='assets/plugins/flot/jquery.flot.spline.js'></script>

	<!-- endbuild -->

	<!-- build:js({.tmp,app}) scripts/scripts.js -->
	<script src="scripts/core/controllers/mainController.js"></script>
	<script src="scripts/core/controllers/messagesController.js"></script>
	<script src="scripts/core/controllers/navigationController.js"></script>
	<script src="scripts/core/controllers/notificationsController.js"></script>
	<script src="scripts/core/directives/directives.js"></script>
	<script src="scripts/core/directives/form.js"></script>
	<script src="scripts/core/directives/ui.js"></script>
	<script src="scripts/core/modules/templateOverrides.js"></script>
	<script src="scripts/core/modules/templates.js"></script>
	<script src="scripts/core/modules/panels/ngDraggable.js"></script>
	<script src="scripts/core/modules/panels/panels.js"></script>
	<script src="scripts/core/modules/panels/directives.js"></script>
	<script src="scripts/core/services/services.js"></script>
	<script src="scripts/core/services/theme.js"></script>
	<script src="scripts/core/theme.js"></script>
	<script src="scripts/calendar/calendar.js"></script>
	<script src="scripts/chart/canvas.js"></script>
	<script src="scripts/chart/flot.js"></script>
	<script src="scripts/chart/morris.js"></script>
	<script src="scripts/chart/sparklines.js"></script>
	<script src="scripts/gallery/gallery.js"></script>
	<script src="scripts/map/googleMaps.js"></script>
	<script src="scripts/map/vectorMaps.js"></script>
	<script src="scripts/demos/modules/basicTables.js"></script>
	<script src="scripts/demos/modules/boxedLayout.js"></script>
	<script src="scripts/demos/modules/calendar.js"></script>
	<script src="scripts/demos/modules/canvasCharts.js"></script>
	<script src="scripts/demos/modules/nvd3Charts.js"></script>
	<script src="scripts/demos/modules/chatBox.js"></script>
	<script src="scripts/demos/modules/editableTable.js"></script>
	<script src="scripts/demos/modules/flotCharts.js"></script>
	<script src="scripts/demos/modules/form/form.js"></script>
	<script
		src="scripts/demos/modules/form/controllers/angularFormValidationController.js"></script>
	<script
		src="scripts/demos/modules/form/controllers/datepickerDemoController.js"></script>
	<script
		src="scripts/demos/modules/form/controllers/dateRangePickerDemoController.js"></script>
	<script
		src="scripts/demos/modules/form/controllers/formComponentsController.js"></script>
	<script
		src="scripts/demos/modules/form/controllers/imageCropController.js"></script>
	<script
		src="scripts/demos/modules/form/controllers/inlineEditableController.js"></script>
	<script
		src="scripts/demos/modules/form/controllers/timepickerDemoController.js"></script>
	<script src="scripts/demos/modules/gallery.js"></script>
	<script src="scripts/demos/modules/googleMaps.js"></script>
	<script src="scripts/demos/modules/horizontalLayout.js"></script>
	<script
		src="scripts/demos/modules/mail/controllers/composeController.js"></script>
	<script src="scripts/demos/modules/mail/controllers/inboxController.js"></script>
	<script src="scripts/demos/modules/mail/mail.js"></script>
	<script src="scripts/demos/modules/morrisCharts.js"></script>
	<script src="scripts/demos/modules/sparklineCharts.js"></script>
	<script src="scripts/demos/modules/ngGrid.js"></script>
	<script src="scripts/demos/modules/panels.js"></script>
	<script src="scripts/demos/modules/registrationPage.js"></script>
	<script src="scripts/demos/modules/signupPage.js"></script>
	<script src="scripts/demos/modules/notFoundController.js"></script>
	<script src="scripts/demos/modules/errorPageController.js"></script>
	<script src="scripts/demos/modules/tasks.js"></script>
	<script src="scripts/demos/modules/ui-components/uiComponents.js"></script>
	<script
		src="scripts/demos/modules/ui-components/controllers/alertsController.js"></script>
	<script
		src="scripts/demos/modules/ui-components/controllers/carouselController.js"></script>
	<script
		src="scripts/demos/modules/ui-components/controllers/modalsController.js"></script>
	<script
		src="scripts/demos/modules/ui-components/controllers/nestableController.js"></script>
	<script
		src="scripts/demos/modules/ui-components/controllers/paginationsController.js"></script>
	<script
		src="scripts/demos/modules/ui-components/controllers/progressbarsController.js"></script>
	<script
		src="scripts/demos/modules/ui-components/controllers/ratingsController.js"></script>
	<script
		src="scripts/demos/modules/ui-components/controllers/slidersController.js"></script>
	<script
		src="scripts/demos/modules/ui-components/controllers/tabsController.js"></script>
	<script
		src="scripts/demos/modules/ui-components/controllers/tilesController.js"></script>
	<script src="scripts/demos/modules/vectorMaps.js"></script>
	<script src="scripts/demos/modules/dashboard.js"></script>
	<script src="scripts/demos/demos.js"></script>
	<script src="scripts/app.js"></script>

	<!-- endbuild -->
	<!-- App-->
	<script src="scripts/app/controllers/addCompanyCtrl.js"></script>
	<script src="scripts/app/controllers/addBranchCtrl.js"></script>
	<script src="scripts/app/controllers/assignRoleCtrl.js"></script>
	<script src="scripts/app/controllers/addUserCtrl.js"></script>
	<script src="scripts/app/controllers/companyManagement.js"></script>
	<script src="scripts/app/controllers/userManagement.js"></script>
	<script src="scripts/app/controllers/branchManagement.js"></script>
	<script src="scripts/app/controllers/customerManagementCtrl.js"></script>
	<script src="scripts/app/controllers/liftManagementCtrl.js"></script>
	<script src="scripts/app/controllers/addLiftCtrl.js"></script>
	<script src="scripts/app/controllers/workListCtrl.js"></script>
	<script src="scripts/app/services/serviceApi.js"></script>
	<script src="scripts/app/services/utility.js"></script>
	<!-- App-->
</body>
</html>