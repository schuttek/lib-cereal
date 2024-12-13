package re.nectar.lib.cereal.example;

import re.nectar.lib.cereal.ByteArray;
import re.nectar.lib.cereal.Cerealizable;

public class Tree implements Cerealizable {

    private float height;
    private int leafCount;
    private String speciesName;


    public Tree() {}

    public Tree(final float height, final int leafCount, final String speciesName) {
        this.height = height;
        this.leafCount = leafCount;
        this.speciesName = speciesName;
    }

    @Override
    public void cerealizeTo(final ByteArray ba) {
        ba.add(height);
        ba.add(leafCount);
        ba.add(speciesName);
    }

    @Override
    public void uncerealizeFrom(final ByteArray ba) {
        height = ba.getFloat();
        leafCount = ba.getInt();
        speciesName = ba.getString();
    }

    public static void main(String[] args) {

        final Tree birch = new Tree(23.68f, 2692, "Birch");
        // cerealize:
        ByteArray ba = new ByteArray();
        birch.cerealizeTo(ba);
        final byte[] buffer1 = ba.getAllBytes();
        // same thing, less typing:
        final byte[] buffer2 = ByteArray.cerealize(birch).getAllBytes();

        ByteArray readBa = new ByteArray(buffer1);
        final Tree uncerealizedBirch1 = new Tree();
        uncerealizedBirch1.uncerealizeFrom(readBa);

        // same thing in short hand notation.
        final Tree uncerealizedBirch2 = ByteArray.wrap(buffer2).uncerealize(Tree.class);

        assert(uncerealizedBirch1.equals(birch));
    }
}
