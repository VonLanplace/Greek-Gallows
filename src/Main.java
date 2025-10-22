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
	private static ArrayList<Character> guessList;
	private static int lifes;
	private static GallowWord word;

	public static void main(String[] args) {
		boolean loop = true;
		while (loop) {
			guessList = new ArrayList<Character>();
			lifes = 5;
			ReadFile readfile;
			try {
				printMainMenu();
				int opc = inputInt("Option(1,2,3): ");
				switch (opc) {
				case 1:
				case 2:
				case 3:
					readfile = new ReadFile(Integer.toString(opc + 2));
					word = new GallowWord(readfile.readWord());
					mainGame();
					break;
				case 9:
					loop = false;
					break;
				case -1:
					clearScreen();
					System.err.println("Digite um número!");
					break;
				default:
					clearScreen();
					break;
				}
			} catch (IllegalArgumentException e) {
				clearScreen();
				System.err.println(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void printMainMenu() {
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
	}

	public static void mainGame() {
		if (word == null)
			throw new IllegalArgumentException("No word Found");

		boolean loop = true;
		while (loop) {
			clearScreen();
			if (testWin()) {
				printWin();
				loop = false;
			} else {
				if (lifes >= 0) {
					printForca(word, guessList, lifes);
					lifes += guessForca();
				} else {
					printLose();
					loop = false;
				}
			}
		}
	}

	private static void printLose() {
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
		text.append("Tua carne alimenta os urubus ó escória de ZeLus!\nGlória a ").append(godName()).append("!");
		System.err.println(text.toString());
		// Hold Term
		inputInt("Press Enter.");
		clearScreen();
	}

	private static void printWin() {
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
		inputInt("Press Enter.");
		clearScreen();

	}

	public static String godName() {
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

	private static boolean testWin() {
		int hits = 0;
		for (int i = 0; i < word.getSize(); i++)
			if (word.wasHit(i))
				hits++;
		if (hits >= word.getSize()) {
			return true;
		}
		return false;
	}

	private static int guessForca() {
		String guess;
		System.out.println();
		System.out.print("Guess: ");
		Scanner in = new Scanner(System.in);

		try {
			guess = in.nextLine();
		} catch (Exception e) {
			in.close();
			in = new Scanner(System.in);
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

	private static int guessWord(String guess, GallowWord word) {
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

	private static int guessChar(char guess, GallowWord word) {
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

	private static void printForca(GallowWord word, ArrayList<Character> guessList, int lifes) {

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

	public static int inputInt(String message) {
		String input;
		System.out.println();
		System.out.print(message);
		Scanner in = new Scanner(System.in);

		try {
			input = in.nextLine();
		} catch (Exception e) {
			in.close();
			in = new Scanner(System.in);
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

	public static void clearScreen() {
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
