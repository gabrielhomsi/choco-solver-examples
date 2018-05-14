import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.util.Arrays;

public class Knapsack {
    public static void main(String[] args) {
        final int[] W = {12, 2, 1, 4, 1}; // item weights
        final int[] P = {4, 2, 1, 10, 2}; // item profits
        final int q = 15; // knapsack capacity

        Model model = new Model("knapsack");

        // create variables
        BoolVar[] X = model.boolVarArray("X", W.length); // X[i]: 1 if item i is packed, 0 otherwise
        //

        // capacity constraint (sum of weights can't exceed total capacity)
        model.scalar(X, W, "<=", q).post(); // X[0] * W[0] + X[1] * W[1] + ... <= q
        //

        // define objective (maximize profit)
        final int zUB = Arrays.stream(P).sum(); // upper bound for the objective value
        IntVar z = model.intVar("z", 0, zUB);
        model.scalar(X, P, "=", z).post(); // z = X[0] * P[0] + X[1] * P[1] + ... = z
        model.setObjective(Model.MAXIMIZE, z);
        //

        // call solver
        model.getSolver().solve();
        //

        // print solution
        for (int i = 0; i < W.length; i++)
            if (X[i].getValue() == 1)
                System.out.println("packed item " + i + " with weight " + W[i] + " and profit " + P[i]);

        System.out.println("profit = " + z.getValue());
        //
    }
}
