package il.ac.hit.costmanager;


import javax.swing.*;

public class Program {
    public static void main(String[] args) throws CostManagerException
    {

        IView view = new View();
        IModel model = new Model();
        IViewModel viewModel = new ViewModel(view ,model);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void  run() {
                    view.initialize();
                    view.start();
            }
        });
        view.setIViewModel(viewModel);
        //Model model = new Model("Natan");


        //model.getAllCategories


        //Collection<CostItem> costs= model.getAllCosts("Natan");
        //costs.forEach(i -> {
        //    System.out.println(i);
        //});
//




    }
}
