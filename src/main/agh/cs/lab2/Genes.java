package agh.cs.lab2;


import java.util.Arrays;

public class Genes {
    public int[] genes = new int[32];

    //Create New Genes
    public Genes()
    {
        for(int i=0; i<genes.length ;i++)
        {
           genes[i] = (int) (Math.random() * 10 % 8);
        }
        fixGenes();
    }

    //Create Child Genes
    public Genes(Genes moddyOne, Genes moddyTwo)
    {
        int dividerOne = (int) (Math.random() * 1000 % 31 + 1);
        int dividerTwo = (int) (Math.random() * 1000 % 31 + 1);
        while(dividerTwo == dividerOne) dividerTwo = (int) (Math.random() * 1000 % 31 + 1);
        if(dividerOne>dividerTwo)
        {
            int swapper = dividerOne;
            dividerOne = dividerTwo;
            dividerTwo = swapper;
        }

        for(int i=0;i<dividerOne;i++)
            genes[i] = moddyOne.genes[i];

        for(int i=dividerOne;i<dividerTwo;i++)
            genes[i] = moddyTwo.genes[i];

        for(int i=dividerTwo;i<genes.length;i++)
            genes[i] = moddyOne.genes[i];

        fixGenes();
    }

    //Fill Missing Genes
    private void fixGenes()
    {
        int[] present = new int[8];
        for(int i=0;i<8;i++)
        {
            present[i] = 0;
        }

        for(int i=0; i<genes.length; i++)
        {
            for(int j=0;j<8;j++)
            {
                if(genes[i] == present[j]) present[j]++;
            }
        }

        for(int i=0;i<8;i++)
        {
            if(present[i]==0)
                for(int j=0;j<genes.length;j++)
                {
                    if(present[genes[j]]>1)
                    {
                        present[genes[j]]--;
                        genes[j] = i;
                    }
                }
        }
    }

    public int getRandomGene()
    {
        return this.genes[(int) (Math.random() * 10000 % this.genes.length)];
    }

    public String toString()
    {
        return Arrays.toString(genes).replaceAll("\\[|\\]|,|\\s", "");
    }
}
