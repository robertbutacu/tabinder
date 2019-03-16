import click

@click.command(add_help_option=False, hidden=True)
@click.option("--count", default=1, help="Number of greetings.")
def hello(count):
	"""Simple program that greets NAME for a total of COUNT times."""
	click.echo("Hello, %s!" % "robert")

@click.command(add_help_option=False, hidden=True)
@click.option("--artist", help="The name of the artist to look up")
def getByArtist(artist):
	"""Retrieve all tabs by the artist name given as parameter"""
	click.echo("FROM THE ARTIST COMMAND " + artist)

def processCommand(command):
	try:
		command()
	except:
		pass

if __name__ == "__main__":
	processCommand(hello)
	
	processCommand(getByArtist)
