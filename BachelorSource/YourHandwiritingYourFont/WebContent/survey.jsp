<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <title>Your Handwriting Your Font</title>
  <link rel="stylesheet" href="css/style.css" type="text/css" />
  <link rel="stylesheet" href="css/main.css" type="text/css" />
  <link rel="stylesheet" href="css/demos.css" type="text/css" />
  <script src="js/jquery.min.js"></script>
  		<!--[if lte IE 8]><script src="js/html5shiv.js"></script><![endif]-->
		<script src="js/skel.min.js"></script>
		<script src="js/skel-panels.min.js"></script>
		<script src="js/init.js"></script>
		<noscript>
			<link rel="stylesheet" href="css/skel-noscript.css" />
			<link rel="stylesheet" href="css/style.css" />
			<link rel="stylesheet" href="css/style-wide.css" />
		</noscript>
  <meta http-equiv="Content-type" content="text/html;charset=UTF-8" />
 
</head>

<body>
  <div id="fb-root"></div>
	<script src="https://connect.facebook.net/en_US/all.js"></script>
  <script>
		FB.init({
			appId : '596116977146222',
			cookie : true,
			status : true,
			xfbml : true
		});
FB.getLoginStatus(function(response) {
	  if (response.status === 'connected') {
			document.getElementById("menu").innerHTML += "<li><a class='skel-panels-ignoreHref'"+
			"href='ttfLoad.do' target='_blank'><span class='fa fa-download'>Download your font</span></a></li><li><a class='skel-panels-ignoreHref' onclick='logout()'><span class='fa fa-unlock'>Logout</span></a></li>";	  
			FB.api('/me/?access_token=${access_token}', 
		    function (response) {
		      if (response && !response.error) {
		    	  document.getElementById("title").innerHTML = "Hi, " + response.first_name;
		      }
		    });
	  }	 
	  else{
	  }
	  });

		 
function PassIDSurvey()
{
			 window.location.replace("survey.jsp");
}

function PassID()
{
			 window.location.replace("upload.jsp");
}

function PassIDHome()
{
			 window.location.replace("index.jsp");
}

function Why()
{
			 window.location.replace("Why.jsp");
}

function Who()
{
			 window.location.replace("Contributers.jsp");
}

function logout(){
	FB.logout(function(response) {
		  alert("Please help us enhance your experience :D");
		  window.location.replace("survey.jsp");
		});
}
</script>

<div id="header" class="skel-panels-fixed">
<div class="top">
				<!-- Logo -->
				<div id="logo">
					<span class="image avatar48"><img src="css/images/fontifi.png" alt="" /></span>
					<h2 id="title">Fontifi</h2>
					<span class="byline">Your Handwriting Your Font</span>
				</div>

<nav id="nav">
<ul id ="menu">
	<li>
	<a class="skel-panels-ignoreHref" onclick="PassIDHome()"><span class="fa fa-home">View Facebook Wall</span></a>
	</li>
	<li id="Link"><a class="skel-panels-ignoreHref" onclick="PassID()"><span class="fa fa-font">Create Font</span></a></li>
	<li><a class="skel-panels-ignoreHref" onclick="PassIDSurvey()"><span class="fa fa-comments">Survey</span></a></li>
	<li><a class="skel-panels-ignoreHref" onclick="Why()"><span class="fa fa-question">Why Fontifi</span></a></li>
	<li><a class="skel-panels-ignoreHref" onclick="Who()"><span class="fa fa-user">Contributers</span></a></li>
	
</ul>
</nav>
</div>
</div>

<div id="main">
<section id="top" class="one">
<div class="container">
<iframe src="https://kwiksurveys.com/s.asp?sid=3zo8wgw7kzxm1n2345491" style="width:100%; height:6000px;"/>
</div>
</section>
</div>
</body>
</html>
