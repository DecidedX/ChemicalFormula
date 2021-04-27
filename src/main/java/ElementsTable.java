import java.util.HashMap;

public class ElementsTable {

    static final HashMap<String,Integer> table = new HashMap<>();
    static {
        table.put("H",1);
        table.put("He",4);

        table.put("Li",7);
        table.put("Be",9);
        table.put("B",11);
        table.put("C",12);
        table.put("N",14);
        table.put("O",16);
        table.put("F",19);
        table.put("Ne",20);

        table.put("Na",23);
        table.put("Mg",24);
        table.put("Al",27);
        table.put("Si",28);
        table.put("P",31);
        table.put("S",32);
        table.put("Cl",35);
        table.put("Ar",40);

        table.put("K",39);
        table.put("Ca",40);
        table.put("Sc",45);
        table.put("Ti",48);
        table.put("V",51);
        table.put("Cr",52);
        table.put("Mn",55);
        table.put("Fe",56);
        table.put("Co",59);
        table.put("Ni",59);
        table.put("Cu",64);
        table.put("Zn",65);
        table.put("Ga",70);
        table.put("Ge",73);
        table.put("As",75);
        table.put("Se",79);
        table.put("Br",80);
        table.put("Kr",84);

        table.put("Rb",85);
        table.put("Sr",88);
        table.put("Y",89);
        table.put("Zr",91);
        table.put("Nb",93);
        table.put("Mo",96);
        table.put("Tc",99);
        table.put("Ru",101);
        table.put("Rh",103);
        table.put("Pd",106);
        table.put("Ag",108);
        table.put("Cd",112);
        table.put("In",115);
        table.put("Sn",119);
        table.put("Sb",122);
        table.put("Te",128);
        table.put("I",127);
        table.put("Xe",131);

        table.put("Cs",133);
        table.put("Ba",137);
        table.put("Hf",178);
        table.put("Ta",181);
        table.put("W",184);
        table.put("Re",186);
        table.put("Os",190);
        table.put("Ir",192);
        table.put("Pt",195);
        table.put("Au",197);
        table.put("Hg",201);
        table.put("Tl",204);
        table.put("Pb",208);
        table.put("Bi",209);
    }

    public static int getMass(String name){
        return table.get(name);
    }

}
