package gb.xokyopo.servlets.ui.local;

import gb.xokyopo.servlets.service.impl.ProductServiceImpl;
import gb.xokyopo.servlets.service.impl.ServiceImpl;
import gb.xokyopo.servlets.service.represantations.CategoryRep;
import gb.xokyopo.servlets.service.represantations.ProductRep;
import gb.xokyopo.servlets.service.ProductsService;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class ProductManipulation implements Serializable {
    private ProductRep productRep;
    @EJB(beanName = "ProductsService")
    private ProductServiceImpl productsService;

    public String editingProduct(ProductRep productRep) {
        this.productRep = productRep;
        return "/product.xhtml";
    }

    public String addProduct() {
        this.productRep = new ProductRep();
        this.productRep.setCategoryRep(new CategoryRep());
        return "/product.xhtml";
    }

    public String deleteProduct(ProductRep productRep) {
        this.productsService.delete(productRep);
        return "/catalog.xhtml";
    }

    public String saveChanges() {
        if (!this.productRep.getName().equals("") && this.productRep.getPrice() >= 0) {
            if (this.productRep.getId() > 0) {
                this.productsService.update(productRep);
            } else {
                this.productsService.add(productRep);
            }
            return "/catalog.xhtml?faces-redirect=true";
        }
        return "/product.xhtml";
    }

    public ProductRep getProductRep() {
        return productRep;
    }

    public void setProductRep(ProductRep productRep) {
        this.productRep = productRep;
    }

    public String cancel() {
        return "/catalog.xhtml?faces-redirect=true";
    }


}
