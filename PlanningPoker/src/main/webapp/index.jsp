<html>

	<head>
		<link rel="stylesheet" href="css/app.css" />
	</head>
	
	<body>
		<h1>New Game</h1>
		<form action="planningpoker.jsp">
			Player Name: <input type="text" name="playerName"/><br>
			<input type="submit" value="Start New Game" />
		</form>
	
		<hr>
		<h1>Existing Game</h1>
		<form action="planningpoker.jsp">
			Player Name: <input type="text" name="playerName"/><br>
			Game Id: <input type="text" name="gameId"/><br>
			<input type="submit" value="Join Existing Game" />
		</form>		
	</body>

</html>
