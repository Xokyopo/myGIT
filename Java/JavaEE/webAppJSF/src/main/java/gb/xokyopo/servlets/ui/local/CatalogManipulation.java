package gb.xokyopo.servlets.ui.local;

import gb.xokyopo.servlets.service.impl.ProductServiceImpl;
import gb.xokyopo.servlets.service.represantations.ProductRep;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class CatalogManipulation implements Serializable {
    @EJB(beanName = "ProductsService")
    private ProductServiceImpl productsService;

    public List<ProductRep> getAll() {
        return this.productsService.getAll();
    }
}
