package rocks.zipcode;

import java.util.Set;

/**
 * ntz main command.
 */
public final class Notez {

    private FileMap filemap;

    public Notez() {
        this.filemap  = new FileMap();
    }
    /**
     * Says hello to the world.
     *
     * @param args The arguments of the program.
     */
    public static void main(String argv[]) {
        boolean _debug = true;
        // for help in handling the command line flags and data!
        if (_debug) {
            System.err.print("Argv: [");
            for (String a : argv) {
                System.err.print(a + " ");
            }
            System.err.println("]");
        }

        Notez ntzEngine = new Notez();

        ntzEngine.loadDatabase();

        try {
            switch (argv[0]) {
                case "-r":
                    try {
                        ntzEngine.generalEntry(argv[1]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("Error: Remember called without note");
                    }
                    break;
                case "-h":
                    ntzEngine.help();
                    break;

                case "-c":
                    try {
                        ntzEngine.catergoryEntry(argv[1], argv[2]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("Error: Category called without category and/or note");
                    }
                    break;

                case "-f":
                    if (argv.length == 2) {
                        ntzEngine.removeCategory(argv[1]);
                    } else if (argv.length == 3) {
                        ntzEngine.removeNote(argv[1], Integer.parseInt(argv[2]));
                    } else {
                        System.err.println("Error: Forget called with improper arguments");
                    }
                    break;

                case "-e":
                    try {
                        ntzEngine.editNote(argv[1], Integer.parseInt(argv[2]), argv[3]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("Error: Edit called with improper arguments");
                    }
                    break;

                default:
                    System.err.printf("%s is not a valid command", argv[0]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            ntzEngine.printResults();
        }
        ntzEngine.saveDatabase();

    }

    private void saveDatabase() {
        filemap.save();
    }

    private void printResults() {
        Set entries = filemap.keySet();
        for(Object key : entries){
            System.out.println(key + ":");
            for (int i = 0; i < filemap.get(key).size(); i++){
                System.out.printf("\t%d) %s\n", i + 1, filemap.get(key).get(i));
            }
        }
    }
    public void loadDemoEntries(){
        filemap.put("General", new NoteList("The Very First Note"));
        filemap.put("note2", new NoteList("A secret second note"));
        filemap.put("category3", new NoteList("Did you buy bread AND eggs?"));
        filemap.put("anotherNote", new NoteList("Hello from Zip Code!"));
    }

    public void catergoryEntry(String category, String note){
        if(!filemap.containsKey(category)){
            filemap.put(category, new NoteList(note));
        } else{
            filemap.get(category).add(note);
        }
    }

    public void generalEntry(String note){
        filemap.get("General").add(note);
    }

    public void removeCategory(String category){
        filemap.remove(category);
    }

    public void removeNote(String catergory, int note){
        filemap.get(catergory).remove(note - 1);
    }

    public void editNote(String category, int noteNum, String note){
        if(filemap.containsKey(category)){
            if(filemap.get(category).size() >= noteNum){
                filemap.get(category).set(noteNum - 1, note);
            } else {
                System.err.println("Error: Note out of range");
            }
        } else{
            System.err.println("Error: Category does not exist");
        }
    }

    public void help(){
        System.out.println("-h: prints out help info");
        System.out.println("-r: takes a string argument and adds it to general notes");
        System.out.println("-c: creates or appends to specified category with specific note");
        System.out.println("-f: forgets a category by name or note by category name and number");
        System.out.println("-e: edits a note specified by category and number");
    }

    private void loadDatabase() {
        filemap.load();
    }
}
