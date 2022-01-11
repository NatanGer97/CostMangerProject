package il.ac.hit.costmanager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewModel implements IViewModel {
    private IModel model;
    private IView view;
    private ExecutorService executorService;
    private String userName;


    public ViewModel(IView iView, IModel iModel){
        this.executorService = Executors.newFixedThreadPool(3);
        setModel(iModel);
        setView(iView);
    }

    @Override
    public void addCostItem(CostItem costItem) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    model.addCost(costItem);
                    getAllCosts(userName);
                } catch (CostManagerException e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(new Message(e.getMessage()));
                        }
                    });
                }
            }
        });
    }

    @Override
    public void getAllCategories() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Category> categoryList = (ArrayList<Category>) model.getAllCategories(userName);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            view.setCategoryList(categoryList);
                        }
                    });
                } catch (CostManagerException e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(new Message(e.getMessage()));
                        }
                    });
                }
            }
        });
    }

    @Override
    public void deleteCostItem(int indexOfCostItemToDelete) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    List<CostItem> allCostsList = (List<CostItem>) model.getAllCosts(userName);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                model.removeCost(allCostsList.get(indexOfCostItemToDelete));
                                getAllCosts(userName);
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        getAllCategories();
                                    }
                                });
                            }catch (CostManagerException e){
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.showMessage(new Message(e.getMessage()));
                                    }
                                });
                            }
                        }
                    });
                } catch (CostManagerException e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(new Message(e.getMessage()));
                        }
                    });
                }
            }
        });
    }

    @Override
    public void logOut(String newUsername) {
        setUserName(newUsername);
        getAllCosts(userName);
        getAllCategories();
    }

    @Override
    public void addNewCategory(String categoryName) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    model.addCategory(new Category(categoryName), userName);
                    getAllCategories();
                } catch (CostManagerException e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(new Message(e.getMessage()));
                        }
                    });
                }
            }
        });
    }

    @Override
    public void getAllCosts(String userName) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    List<CostItem> allCostsList = (List<CostItem>) model.getAllCosts(userName);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            view.showAllCosts(allCostsList);
                        }
                    });
                } catch (CostManagerException e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(new Message(e.getMessage()));
                        }
                    });
                }
            }
        });
    }

    @Override
    public void setModel(IModel model) {
        this.model = model;
    }

    @Override
    public void setView(IView view) {
        this.view = view;
    }

    @Override
    public void setUserName(String userName) {
        try {
            model.connectUser(userName);
        } catch (CostManagerException e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    view.showMessage(new Message(e.getMessage()));
                }
            });
        }
        this.userName = userName;
    }

    @Override
    public void getFilteredCosts(String userName, String startDate, String endDate) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    List<CostItem> filteredCostsList = (List<CostItem>) model.getCostByDateRange(startDate, endDate, userName);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            view.showFilteredCosts(filteredCostsList);
                        }
                    });
                } catch (CostManagerException e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(new Message(e.getMessage()));
                        }
                    });
                }

            }
        });
    }
}



