<html>

<head>

   	<script>
var id = <%=request.getParameter("gameId")%>;
var playerName = '<%=request.getParameter("playerName")%>';   	   	
function init() {
	var players = [];
   	var observer = {
   		newGame : function(param){
   			// entra aqui apenas quando � o gerente criando o game
   			id = param.id;
   			document.getElementById("gameId").innerHTML = id;
   			showNewPlayButton();
   		},
   		newPlayerHasEnteredInTheGame : function(player) {
   			players.push(player.name);
   			addPlayerInResultDiv(player.name);
   		},	
   		newPlayHasBeenInitiated : function(play) {
   			document.getElementById("yourVoteDiv").className = "c";
   			reDrawResultDiv();
   		},
   		gameHasBeenFinished : function(param) {
   			alert("Manager has finished this Game! Good by!!!")
   		 	window.location.href = "/index.jsp"
   		},
   		playerHasVoteInCurrentPlay : function(player) {
   			document.getElementById(player.name).className = "back";			
   		},
   		showResult : function(play) {
   			var plays = play.plays;
   			for(i=0; i<plays.length; i++) {
   				var player = plays[i];
   				document.getElementById(player.name).className = "c" + player.points;
   			}			
   		},
   		playerIsOffline : function(player) {
   			// remove do array
   			players.splice(players.indexOf(player.name), 1);
   			
   			// remove visualmente
   			var div = document.getElementById(player.name);
   			div.parentNode.removeChild(div);
   		},
   		onError : function(param) {
   			alert(param.message)
   		 	window.location.href = "/index.jsp"
   		}
   	};
   	
    function PPWebSocket() {
   		var ready = false;
	   	var socket = new WebSocket("ws://localhost:8080/planningpoker?gameId=" + id + "&playerName=" + playerName);
	   	socket.onopen = function(){
	   		console.log('Connection opened');
	   		ready = true;
	   	}
	   	socket.onclose = function(){
	   		console.log('Connection closed');
	   		ready = false;
	   	}
	   	socket.onerror = function(error) {
	   		console.log("Erro acessando " + error.target.URL );
	   		console.log(error);
	   		ready = false;
	   	} 
	   	socket.onmessage = function(event) {
	   		var jsFun = JSON.parse(event.data);
	   		eval("observer." + jsFun.name)(jsFun.params);
	   	}
	   	return {
	    	send : function(data) {
	    		if (ready == true) {
	    			socket.send(data);
	    		}
	    	}
	    }
   	}   	
   	var wsSocket = PPWebSocket();
   	
   	vote = function(points){
   		document.getElementById("yourVoteDiv").className = "c"+points;
   		var obj = {action: 'vote'
   				, gameId: id
   				, playerName: playerName
   				, points: points};
   		wsSocket.send(JSON.stringify(obj));
   	}

   	newPlay = function(){
   		document.getElementById("yourVoteDiv").className = "c"; //FIXME: se n�o for o gerente.. como faz?
   		var obj = {action: 'newPlay'
   				, gameId: id
   				, playerName: playerName};
   		wsSocket.send(JSON.stringify(obj));
   	}
   	
   	showIncompleteResult = function(){
   		var obj = {action: 'showIncompleteResult'
				, gameId: id
				, playerName: playerName};
		wsSocket.send(JSON.stringify(obj));   		
   	}
   	  		
   	showNewPlayButton = function(){
   		document.getElementById("buttonsDiv").innerHTML = 
   			'<input type="button" id="newPlay" value="New Play" onclick="newPlay()" />' +
   			'<input type="button" id="showResult" value="Show Result" onclick="showIncompleteResult()" />';			
   	}



   	addPlayerInResultDiv = function(playerName){
   		var div = document.createElement("div");
   		div.setAttribute("id", playerName );
   		div.setAttribute("title", playerName );
   		div.setAttribute("class", "c0");
   		var result = document.getElementById("resultDiv");
   		result.appendChild(div);
   	}

   	reDrawResultDiv = function() {
   		var result = document.getElementById("resultDiv");
   		result.innerHTML = "";
   		for(i=0; i<players.length; i++) {
   			addPlayerInResultDiv(players[i]);
   		}
   	}
}
   	</script>	
   	
   	<link rel="stylesheet" href="css/app.css"></link>	
</head>

<body onload="init()">
	<h1>Session Number: <span id="gameId"><%=request.getParameter("gameId")%></span></h1>
	<div id="buttonsDiv" ></div>

	<h1>Cards</h1>
	<div class="container">
		<div class="c1" onclick="vote(1)">&nbsp;</div>
		<div class="c2" onclick="vote(2)">&nbsp;</div>
		<div class="c3" onclick="vote(3)">&nbsp;</div>
		<div class="c5" onclick="vote(5)">&nbsp;</div>
		<div class="c8" onclick="vote(8)">&nbsp;</div>
		<div class="c13" onclick="vote(13)">&nbsp;</div>				
		<div class="c20" onclick="vote(20)">&nbsp;</div>
		<div class="c99" onclick="vote(99)">&nbsp;</div>
		<div class="c999" onclick="vote(999)">&nbsp;</div>
	</div>	
	
	<div class="break" ><h1>Your Vote</h1></div>
	<div class="container">
		<div id="yourVoteDiv" class="c">&nbsp;</div>
	</div>
	
	<div class="break" ><h1>Result</h1></div>	
	<div id="resultDiv" class="container" />
	
</body>

</html>
