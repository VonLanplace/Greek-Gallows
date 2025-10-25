import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		boolean loop = true;
		View view = new View();
		Scanner terIn = new Scanner(System.in);
		while (loop) {
			try {
				// Main Menu printer
				view.printMainMenu();

				// Main menu input
				String input = terIn.nextLine();
				int opc = Integer.parseInt(input);

				// Main Menu cases
				switch (opc) {
				case 1:
					view.mainGame(terIn, Integer.toString(opc + 2));
					break;
				case 2:
					view.mainGame(terIn, Integer.toString(opc + 2));
					break;
				case 3:
					view.mainGame(terIn, Integer.toString(opc + 2));
					break;
				case 9:
					loop = false;
					break;
				case -1:
					view.clearScreen();
					System.err.println("Digite um número!");
					break;
				default:
					view.clearScreen();
					break;
				}

			} catch (NumberFormatException e) {
				view.clearScreen();
				System.err.println("Insira um numero Inteiro!");
			} catch (IllegalArgumentException e) {
				view.clearScreen();
				e.printStackTrace();
				System.err.println(e.getMessage());
			} catch (IOException e) {
				view.clearScreen();
				e.printStackTrace();
				System.err.println("File " + e.getMessage() + " not Found!");
			}
		}
		terIn.close();
	}

}

class View {
	public View() {
		super();
	}

	public void printMainMenu() {
		StringBuilder menu = new StringBuilder();
		menu.append("\nÓ Fatecanuz proveis para aos sofí que conheceis os Acanos Komputarios.");
		menu.append("\nGita ó Fatecanuz ou pedeceis na FORCA!");

		menu.append("\n");
		// Léxi = Palavras
		// grámatas = letras
		menu.append("\n 1 - Léxi de grámatas leves");
		menu.append("\n 2 - Léxi de grámatas medianas");
		menu.append("\n 3 - Léxi de grámatas pesadas");

		System.out.println(menu.toString());
		System.out.print("\nOpcoes: ");
	}

	public int inputInt(Scanner termIn, String message) {
		String input;
		System.out.println();
		System.out.print(message);
		try {
			input = termIn.nextLine();
		} catch (Exception e) {
			input = "";
		}

		int opc;
		try {
			opc = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			opc = -1;
		}

		return opc;
	}

	public void mainGame(Scanner termIn, String filename) throws IOException {
		ArrayList<Character> guessList = new ArrayList<Character>();
		int lifes = 5;
		GallowWord word = null;

		try {
			ReadFile readfile = new ReadFile(filename);
			word = new GallowWord(readfile.readWord());
		} catch (IOException e) {
			throw e;
		}

		Controlller controlller = new Controlller();

		boolean loop = true;
		while (loop) {
			clearScreen();
			if (controlller.testWin(word)) {
				printWin(termIn, word);
				loop = false;
			} else {
				if (lifes >= 0) {
					printForca(word, guessList, lifes);
					lifes += controlller.guessForca(termIn, guessList, word);
				} else {
					printLose(termIn, word);
					loop = false;
				}
			}
		}
	}

	public void printLose(Scanner terIn, GallowWord word) {
		// Print forca
		StringBuilder text = new StringBuilder();
		text.append("\n|---|");
		text.append("\n|   o");
		text.append("\n|  /|\\");
		text.append("\n|  / \\");

		text.append("\n|");
		text.append("\n|");

		word.hitAll();
		for (int i = 0; i < word.getSize(); i++)
			if (word.wasHit(i))
				text.append(" ").append(word.getLetter(i));
			else
				text.append(" _");
		System.out.println(text.toString());

		// Victory message
		text = new StringBuilder();
		text.append("\n");
		text.append("Tua carne alimenta os urubus o escoria de ZeLus!\nGloria a ").append(godName()).append("!");
		System.err.println(text.toString());
		// Hold Term
		System.out.print("Press Enter!");
		terIn.nextLine();
		clearScreen();
	}

	public void printWin(Scanner terIn, GallowWord word) {
		// Print forca
		StringBuilder text = new StringBuilder();
		text.append("\n|---|");
		text.append("\n|");
		text.append("\n|  \\o/");
		text.append("\n|   | ");
		text.append("\n|  / \\");
		text.append("\n|");

		for (int i = 0; i < word.getSize(); i++)
			if (word.wasHit(i))
				text.append(" ").append(word.getLetter(i));
			else
				text.append(" _");
		System.out.println(text.toString());

		// Victory message
		text = new StringBuilder();

		text.append("\n");
		text.append("Provastes tua inocencia perante ").append(godName()).append("!\nOrgulhate ó Fatekanuz!");
		System.err.println(text.toString());

		// Hold Term
		System.out.print("Press Enter!");
		terIn.nextLine();
		clearScreen();
	}

	public String godName() {
		int opc = (int) (Math.random() * 6);
		switch (opc) {
		case 0:
			return "Kolevariuz";
		case 1:
			return "Uellinst";
		case 2:
			return "Vekavel";
		case 3:
			return "Krustin";
		case 4:
			return "Kaldinus";
		case 5:
			return "Daciel";
		default:
			return "Rucianus";
		}
	}

	private void printForca(GallowWord word, ArrayList<Character> guessList, int lifes) {

		// Print forca
		StringBuilder text = new StringBuilder();
		text.append("| ").append(lifes).append(" | ").append(word.getHint());
		text.append("\n|---|");

		switch (lifes) {
		case 5:
			text.append("\n| \n| \n|");
			break;
		case 4:
			text.append("\n|   o\n|\n|");
			break;
		case 3:
			text.append("\n|   o\n|   |\n|");
			break;
		case 2:
			text.append("\n|   o\n|  /|\n|");
			break;
		case 1:
			text.append("\n|   o\n|  /|\\\n|");
			break;
		case 0:
			text.append("\n|   o\n|  /|\\\n|  /");
			break;
		case -1:
			text.append("\n|   o\n|  /|\\\n|  / \\");
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + lifes);
		}

		text.append("\n|");
		text.append("\n|");
		for (int i = 0; i < word.getSize(); i++)
			if (word.wasHit(i))
				text.append(" ").append(word.getLetter(i));
			else
				text.append(" _");
		System.out.println(text.toString());

		// Print guessed
		text = new StringBuilder();
		for (int i = 0; i < guessList.size(); i++)
			text.append(guessList.get(i)).append(" ");
		System.err.println(text.toString());
	}

	public void clearScreen() {
		try {
			final String os = System.getProperty("os.name").toLowerCase();

			if (os.contains("windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else {
				// Linux, Unix, macOS
				System.out.print("\033[H\033[2J");
				System.out.flush();

			}
		} catch (final Exception e) {
			System.out.println("\n".repeat(50));
		}
	}

}

class Controlller {
	public Controlller() {
		super();
	}

	public boolean testWin(GallowWord word) {
		int hits = 0;
		for (int i = 0; i < word.getSize(); i++)
			if (word.wasHit(i))
				hits++;
		if (hits >= word.getSize()) {
			return true;
		}
		return false;
	}

	public int guessForca(Scanner termIn, ArrayList<Character> guessList, GallowWord word) {
		String guess;
		System.out.println();
		System.out.print("Guess: ");

		try {
			guess = termIn.nextLine();
		} catch (Exception e) {
			termIn = new Scanner(System.in);
			guess = "";
		}

		guess = guess.toLowerCase();

		if (guess.isEmpty())
			return -1;

		if (guess.length() > 1) {
			return guessWord(guess, word);
		} else {
			char letter = guess.charAt(0);

			if (guessList.contains(letter))
				return 0;
			guessList.add(letter);
			return guessChar(letter, word);
		}
	}

	public int guessWord(String guess, GallowWord word) {
		char[] letters = guess.toCharArray();
		int wordSize = word.getSize();

		int hits = 0;
		if (letters.length == wordSize) {
			for (int i = 0; i < wordSize; i++) {
				if (letters[i] == word.getLetter(i))
					hits++;
			}
		}

		if (hits >= wordSize) {
			word.hitAll();
			return 0;
		}

		return -1;
	}

	public int guessChar(char guess, GallowWord word) {
		boolean hit = false;

		for (int i = 0; i < word.getSize(); i++) {
			if (Character.compare(guess, word.getLetter(i)) == 0) {
				word.hit(i);
				hit = true;
			}
		}

		if (hit)
			return 0;
		else
			return -1;
	}

}

class GallowWord {
	private String hint;
	private char[] letters;
	private boolean[] wasHit;
	private int length;

	public GallowWord(String[] inString) throws IllegalArgumentException {
		String word = inString[0];
		this.hint = inString[1];

		if (word == null)
			throw new IllegalArgumentException("No word Found");

		word = word.toLowerCase();
		this.letters = word.toCharArray();
		this.length = letters.length;

		this.wasHit = new boolean[this.length];
		for (int i = 0; i < this.length; i++)
			this.wasHit[i] = false;
	}

	public char getLetter(int position) {
		return letters[position];
	}

	public void hit(int position) {
		this.wasHit[position] = true;
	}

	public boolean wasHit(int position) {
		return this.wasHit[position];
	}

	public int getSize() {
		return this.length;
	}

	public void hitAll() {
		for (int i = 0; i < this.length; i++)
			this.wasHit[i] = true;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	@Override
	public String toString() {
		StringBuilder word = new StringBuilder();

		for (char letter : this.letters)
			word.append(letter);

		return word.toString();
	}
}

class ReadFile {
	private String filePath;
	private long lineCount;

	public ReadFile(String difficulty) throws IOException {
		StringBuilder filePath = new StringBuilder("dificuldade_");
		difficulty = difficulty.toLowerCase();
		filePath.append(difficulty).append(".txt");
		this.filePath = filePath.toString();
		fileSize();
	}

	public String[] readWord() throws FileNotFoundException, IOException {
		if (!checkFile(filePath)) {
			return new String[] { "abc", "ERRO DIGITE: abc" };
		}
		try (FileReader file = new FileReader(filePath)) {
			BufferedReader reader = new BufferedReader(file);
			int lineNumber = (int) (Math.random() * (lineCount) + 1);

			String outString = null;
			for (int i = 0; i < lineNumber; i++) {
				outString = reader.readLine();
			}

			String[] outVec = outString.split(",");

			if (outVec.length == 1)
				outVec = new String[] { outVec[0], "No Hint" };

			for (String word : outVec)
				word.trim();

			return outVec;
		} catch (Exception e) {
			throw e;
		}
	}

	public void fileSize() throws IOException {
		try {
			this.lineCount = Files.lines(Paths.get(this.filePath)).count();
		} catch (IOException e) {
			throw e;
		}
	}

	public static boolean checkFile(String filePath) {
		try {
			if (!filePath.toLowerCase().endsWith(".txt")) {
				filePath += ".txt";
			}

			File file = new File(filePath);

			if (file.exists()) {
				return true;
			}

			boolean created = file.createNewFile();
			return created;

		} catch (IOException e) {
			System.err.println("Erro ao criar arquivo: " + e.getMessage());
			return false;
		} catch (SecurityException e) {
			System.err.println("Erro de segurança: " + e.getMessage());
			return false;
		}
	}
}
