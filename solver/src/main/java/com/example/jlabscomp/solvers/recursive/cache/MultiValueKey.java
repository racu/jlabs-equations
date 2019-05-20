package com.example.jlabscomp.solvers.recursive.cache;

import java.util.Arrays;

public class MultiValueKey {
    int[] values;
    int hash;

    public MultiValueKey(int v1, int v2, int v3){
        this.values = new int[]{v1,v2,v3};
        hash = Arrays.hashCode(this.values);
    }

    @Override
    public boolean equals(Object o) {
        int[] otherArr = ((MultiValueKey) o).values;
        if(otherArr.length != this.values.length)
            return false;
        for(int i=0; i<otherArr.length; i++)
            if(otherArr[i] != this.values[i])
                return false;
        return true;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
