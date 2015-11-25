<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Your Handwriting Your Font</title>
<link rel="stylesheet" href="css/WallPostsStyles.css" type="text/css" />
<link rel="stylesheet" href="css/style.css" type="text/css" />
<link rel="stylesheet" href="css/main.css" type="text/css" />
<link rel="stylesheet" href="css/demos.css" type="text/css" />
<meta http-equiv="Content-type" content="text/html;charset=UTF-8" />
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

</head>
<body>

	<div id="fb-root"></div>
	<script src="https://connect.facebook.net/en_US/all.js"></script>
	<script>
		var access_token;
		var userID;
		var graphPOSTS;
		var since = 1;
		var DivID = 0;
		var user = new Array();
		var unti= Math.round(Number(new Date())/1000);
		var users = 1;
		FB.init({
			appId : '596116977146222',
			cookie : true,
			status : true,
			xfbml : true
		});
		FB.getLoginStatus(function(response) {
			  if (response.status === 'connected') {
				document.getElementById("wall").innerHTML = "";
			    // the user is logged in
			    userID = response.authResponse.userID;
			    access_token = response.authResponse.accessToken;
				$.ajax({
					url : 'IdPassing',
					type : "POST",
					data : ({
						userID : userID,accessToken: access_token
					}),
					success : function() {
					}
				});
				LoadFont(userID);
				document.getElementById("menu").innerHTML += "<li><a class='skel-panels-ignoreHref'"+
				"href='ttfLoad.do' target='_blank'><span class='fa fa-download'>Download your font</span></a></li><li><a class='skel-panels-ignoreHref' onclick='logout()'><span class='fa fa-unlock'>Logout</span></a></li>";
				document.getElementById("loadb").innerHTML += "<div id='LoadDiv' class='loadmore' onclick='LoadMore()'><p id='LoadButton'>Load More Posts</p></div>";
				FB.api('/me/feed/?access_token='
						+ access_token, 
			    function (response) {
			      if (response && !response.error) {
			    	  DataReady(response.data);
			      }
			    });
				FB.api('/me/?access_token='
						+ access_token, 
			    function (response) {
			      if (response && !response.error) {
			    	  document.getElementById("title").innerHTML = "Hi, " + response.first_name;
			      }
			    });
			  }else {
				  FB.login(function(response) {
						if (response.authResponse) {
							document.getElementById("wall").innerHTML = "";
							access_token = response.authResponse.accessToken;
							userID = response.authResponse.userID;
							$.ajax({
								url : 'IdPassing',
								type : "POST",
								data : ({
									userID : userID,accessToken: access_token
								}),
								success : function() {
								}
							});
							LoadFont(userID);
							document.getElementById("menu").innerHTML += "<li><a class='skel-panels-ignoreHref' onclick='logout()'><span class='fa fa-unlock'>Logout</span></a></li><li><a class='skel-panels-ignoreHref'"+
							"href='ttfLoad.do' target='_blank'><span class='fa fa-download'>Download your font</span></a><li>";
							document.getElementById("loadb").innerHTML += "<div id='LoadDiv' class='loadmore' onclick='LoadMore()'><p id='LoadButton'>Load More Posts</p></div>";
							FB.api('/me/feed/?access_token='
									+ access_token,
						    function (response) {
						      if (response && !response.error) {
						    	  DataReady(response.data);
						      }
						    });

							FB.api('/me/?access_token='
									+ access_token, 
						    function (response) {
						      if (response && !response.error) {
						    	  document.getElementById("title").innerHTML = "Hi, " + response.first_name;
						      }
						    });
							
						} else {
							document.getElementById("LoadDiv").onclick = null;
							alert("Application not authorized !");
						}
					}, {
						scope : "read_stream,publish_stream,user_status"
					});
			  }
			 });
		
		function LoadMore() {
			document.getElementById("LoadDiv").innerHTML = "<img src='loading.gif' style='width:20%;'>";
			unti = unti - 2629743;
			FB.api('/me/feed/?access_token='
					+ access_token+'&limit=50&until='+unti,
		    function (response) {
		      if (response && !response.error) {
		    	  DataReady(response.data);
		    	  document.getElementById("LoadDiv").innerHTML = "<p id='LoadButton'>Load More Posts</p>";
		      }
		    });
		}

		function DataReady(ThePosts) {
			//console.log(ThePosts);
			if (ThePosts.length == 0) {
				document.getElementById("LoadButton").innerHTML = 'No More Posts to show !';
				document.getElementById("LoadDiv").onclick = null;
				return;
			}
			var post;
			var SenderID;
			until = ThePosts[ThePosts.length - 1].created_time - 2;
			for ( var i = 0; l = ThePosts.length, i < l; i++) {
				post = ThePosts[i];
					SenderID = post.from.id;
					post.updated_time = relativeTime(post.updated_time);
					if(post.story){
					
					}else{
					if (post.message) {
						post.message = urlHyperlinks(post.message);
					}
					
					fbUser(post, SenderID, function(ThePost, user) {
							PrintPost(ThePost, user, SenderID);
						DivID = DivID + 1;
					
					});
					}
			}
			;
		}

		function expand(n) {
			var Div = document.getElementById(n + "comments");
			var button = document.getElementById("button" + n);
			if (Div.style.display == "none") {
				button.childNodes[0].nodeValue = "Hide Comments";
				Div.style.display = 'block';
			} else {
				button.childNodes[0].nodeValue = "Show Comments";
				Div.style.display = 'none';
			}

		}
		
		function logout(){
			FB.logout(function(response) {
				  alert("Please help us enhance your experience :D");
				  window.location.replace("survey.jsp");
				});
		}

		function PrintPost(post, user, SenderID) {
			var TheWall = document.getElementById("wall");
			var CurrentDiv;
			TheWall.innerHTML += "<div id = " + DivID + " class = 'WallPost'>"
					+ "<img class='picture' src='"+user.picture.data.url+"'>"
					+ "<b class='prof'><a href="+user.link+" target='_blank'>" + user.name
					+ "</a></b>" + "<br><p class='date'>" + post.updated_time + "</p>"
					+ "<img src='img/divider.png' class='divider'> </div>";
			CurrentDiv = document.getElementById(DivID);
			if (post.message) {
				LoadFont(post.from.id);
				CurrentDiv.innerHTML += "<p class ='message' style='font-size:25px;font-family:font"+ post.from.id+ "'>"
						+ post.message + " </p>";
			}

			if (post.type == "video") {
				var link = post.source.replace("autoplay=1", "autoplay=0");
				CurrentDiv.innerHTML += "<div class='attachment'> <video controls name='media' style='width:600px;'> <source src=" + link
	            					+ " type='video/mp4'> </video> </div>";
			} else if (post.type == "link" || post.type == "photo") {
				var attachment = "<div class='attachment'>"
						+ "<a href="+post.link + " target='_blank'><img src="+post.picture+" style='width:600px; height:600px;' border='2' /></a>"
						+ "<div class='attachment-data'>";
						
				if (post.link && post.name) {
					attachment += "<p class='name'><a href=" + post.link + " target='_blank'>"
							+ post.name + "</a></p>";
				}
				if (post.caption) {
					attachment += "<p class='caption'>" + post.caption + "</p>";
				}
				if (post.description) {
					attachment += "<p class='description'>" + post.description
							+ "</p>";
				}
				if(post.story){
					attachment += "<p class='description'>" + post.story
					+ "</p>";
				}
				CurrentDiv.innerHTML += attachment + "</div></div>";
			}
			CurrentDiv.innerHTML += " <p id='"+post.id+"like' class='meta'> people like this</p>";

			getLikes(CurrentDiv, post.id);

			CurrentDiv.innerHTML += "<p > <a id='" + post.id
					+ "likebutton' onclick=Like('" + post.id
					+ "',this) class='meta2' ><span class='fa fa-thumbs-up'>like</span></a> </p>";

			//CurrentDiv.innerHTML += "<BR>&nbsp;<BR>";

			CurrentDiv.innerHTML += "<textarea id='textarea" + DivID
					+ "' class='TextFields' rows='1' onkeyup=ExpandArea(this) "
					+ "style='font-family:font" + userID + "'></textarea>";
			CurrentDiv.innerHTML += "<button class='CommentButton' style='float:right;' onclick=AddComment('"
					+ DivID + "','" + post.id + "')>Comment</button>";
			CurrentDiv.innerHTML += "<button id='button"
						+ DivID
						+ "' class='buttonShowComments' style='display:none;' onclick=expand('"
						+ DivID
						+ "')>Show Comments</button><div id = " + DivID + "comments" +" class = 'Comment' style='display:none;'> </div>";
				if (post.comments) {
					displayComments(DivID, post.comments);
				}
			TheWall.innerHTML += "<BR>&nbsp;<BR>";
		}

		function ExpandArea(textArea) {
			while (textArea.rows > 1
					&& textArea.scrollHeight < textArea.offsetHeight) {
				textArea.rows--;
			}

			while (textArea.scrollHeight > textArea.offsetHeight) {
				textArea.rows++;
			}
		}

		function displayComments(CurrentDiv, comments) {
			var comment;
			var button = document.getElementById("button" + CurrentDiv);
			button.style.display = 'block';
			for ( var i = 0; i < comments.data.length; i++) {
				comment = comments.data[i];
				if (comment.message) {
					comment.message = urlHyperlinks(comment.message);
				}
				fbUser(comment, comment.from.id, function(TheComment, user) {
					ShowCommentWithUser(TheComment, user, CurrentDiv);
				});
			}
		}

		function LoadFont(userID) {
			for ( var i = 0; i < users; i++) {
				if (user[i] == userID) {
					return;
				}
			}
			user[users] = userID;
			users = users + 1;
				var style = document.createElement('style');
				style.type = 'text/css';
				style.innerHTML = '@font-face {font-family: font' + userID
						+ ';src: url(ttfLoad.do?'+userID+');}';
				document.getElementsByTagName('head')[0].appendChild(style);
		}

		function ShowCommentWithUser(TheComment, user, DivID) {
			var CurrentDiv = document.getElementById(DivID + "comments");
			LoadFont(TheComment.from.id);
			CurrentDiv.innerHTML += "<div id = " + DivID + "comments" + TheComment.id + ">"
					+ "<img class='Commentpicture' src='"+user.picture.data.url+"'> "
					+ "<b><a style='float:left; font-size:14px;' href="+user.link+" target='_blank'> "
					+ user.name
					+ "</a></b><br>"
					+ "<p class='metaComment' style='float:left; word-wrap:break-word; margin-left:10px; width:100%; text-align:left; font-size: 14pt; font-family:"+ TheComment.from.id+ "'> "
					+ TheComment.message + "</p>";
			var theCommentDiv = document.getElementById(DivID + "comments"
					+ TheComment.id);
			TheComment.created_time = relativeTime(Date
					.parse(TheComment.created_time));
			var footer = "<br><div class='metaComment'> <a>"
					+ TheComment.created_time
					+ " .</a> <a id='"
					+ TheComment.id
					+ "likebutton' onclick=Like('"
					+ TheComment.id + "',this) > like </a>";
			getLikes(CurrentDiv, TheComment.id);
			footer += "<a id='"+TheComment.id+"like' class='metafooter'> people like this <a/>";

			footer += "</div> <img class='divider' src='img/divider.png'>";
			theCommentDiv.innerHTML += footer;
		}

		function fbUser(post, SenderID, callback) {
			FB.api(SenderID + '?fields=name,link,picture', function(response) {
				//console.log(response);
				callback(post, response);
			});
		}

		function getLikes(CurrentDiv, ID) {
			FB.api('/' + ID + '/likes', function(result) {
				//console.log(result);
				AddLikes(ID, result, CurrentDiv);
			});
		}

		function AddLikes(ID, likes, CurrentDiv) {
			if (likes.data.length == 0) {
				document.getElementById(ID + "like").innerHTML = "";
			} else {
				for ( var i = 0; i < likes.data.length; i++) {
					if (likes.data[i].id == userID) {
						var num = likes.data.length - 1;
						if (num == 0) {
							document.getElementById(ID + "like").innerHTML = "You Like this";
						} else {
							document.getElementById(ID + "like").innerHTML = "You and "
									+ num
									+ document.getElementById(ID + "like").innerHTML;
						}
						document.getElementById(ID + "likebutton").style.display = 'none';
						return;
					}
				}
				document.getElementById(ID + "like").innerHTML = likes.data.length
						+ document.getElementById(ID + "like").innerHTML;
			}

		}

		function AddComment(DivID, PostID) {
			var TextField = document.getElementById("textarea" + DivID);
			var comment = TextField.value;
			TextField.value = "";

			FB.api('/' + PostID + '/comments?message=' + comment, 'post',
					function(result) {
						console.log("EL result beta3 posting");
						console.log(result);
						PostComment(result.id, DivID);

					});
		}

		function PostComment(ID, CurrentDiv) {
			var button = document.getElementById("button" + CurrentDiv);
			if (button.style.display == "none") {
				button.style.display = 'block';
			}
			FB.api('/' + ID, function(result) {
				console.log("EL result beta3 post comment");
				console.log(result);
				fbUser(result, userID, function(TheComment, user) {
					ShowCommentWithUser(TheComment, user, CurrentDiv);
				});
			});
		}

		function Like(ID, link) {

			FB.api('/' + ID + '/likes', 'post', function(result) {
				//console.log(result);
			});
			link.style.display = 'none';
			if (document.getElementById(ID + "like").innerHTML == "") {
				document.getElementById(ID + "like").innerHTML = "You Like this";
			} else {
				document.getElementById(ID + "like").innerHTML = "You and "
						+ document.getElementById(ID + "like").innerHTML;

			}
		}

		function PassID() {
			$.ajax({
				url : 'IdPassing',
				type : "POST",
				data : ({
					userID : userID,accessToken: access_token
				}),
				success : function() {
					window.location.replace("upload.jsp");
				}
			});
		}

		function PassIDSurvey() {
			$.ajax({
				url : 'IdPassing',
				type : "POST",
				data : ({
					userID : userID,accessToken: access_token
				}),
				success : function() {
					window.location.replace("survey.jsp");
				}
			});
		}
		
		function Why()
		{
					 window.location.replace("Why.jsp");
		}

		function Who()
		{
					 window.location.replace("Contributers.jsp");
		}

		function urlHyperlinks(str) {
			return str.replace(/\b((http|https):\/\/\S+)/g,
					'<a href="$1" target="_blank">$1</a>');
		}

		function relativeTime(time) {

			// Adapted from James Herdman's http://bit.ly/e5Jnxe

			var period = new Date(time);
			var delta = new Date() - period;
			return period.toLocaleString();

		}
	</script>

	<div id="header" class="skel-panels-fixed">
		<div class="top">
			<!-- Logo -->
			<div id="logo">
				<span class="image avatar48"><img
					src="css/images/fontifi.png" alt="" /></span>
				<h2 id="title">Fontifi</h2>
				<span class="byline">Your Handwriting Your Font</span>
			</div>

			<nav id="nav">
				<ul id="menu">
					<li><a class="skel-panels-ignoreHref"><span
							class="fa fa-home">View Facebook Wall</span></a></li>
					<li id="Link"><a class="skel-panels-ignoreHref"
						onclick="PassID()"><span class="fa fa-font">Create Font</span></a></li>
					<li><a class="skel-panels-ignoreHref" onclick="PassIDSurvey()"><span
							class="fa fa-comments">Survey</span></a></li>
	<li><a class="skel-panels-ignoreHref" onclick="Why()"><span class="fa fa-question">Why Fontifi</span></a></li>
	<li><a class="skel-panels-ignoreHref" onclick="Who()"><span class="fa fa-user">Contributers</span></a></li>	
		
				</ul>
			</nav>
		</div>


	</div>

	<div id="main">
		<section id="top" class="one">
			<div id="loadb" class="container">
				<div id="wall"><img src="Compare.png"/></div>
			</div>
		</section>
	</div>

</body>
</html>