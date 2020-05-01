package gb.xokyopo.servlets.ui.local;

import gb.xokyopo.servlets.service.impl.ServiceImpl;
import gb.xokyopo.servlets.service.represantations.CategoryRep;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class CategoryManipulation implements Serializable {
    private CategoryRep categoryRep;
    @EJB(beanName = "CategoryService")
    private ServiceImpl<CategoryRep> categoryService;

    public String addCategory() {
        this.categoryRep = new CategoryRep();
        return "/category.xhtml";
    }

    public String editingCategory(CategoryRep categoryRep) {
        this.categoryRep = categoryRep;
        return "/category.xhtml";
    }

    public String deletingCategory(CategoryRep categoryRep) {
        this.categoryService.delete(categoryRep);
        return "/categories.xhtml?faces-redirect=true";
    }

    public String saveChange() {
        if (!this.categoryRep.getName().equals("")) {
            if (this.categoryRep.getId() > 0) {
                this.categoryService.update(this.categoryRep);
            } else {
                this.categoryService.add(this.categoryRep);
            }
            return "/categories.xhtml?faces-redirect=true";
        }
        return "/category.xhtml";
    }

    public String cancel() {
        return "/categories.xhtml?faces-redirect=true";
    }

    public CategoryRep getCategoryRep() {
        return categoryRep;
    }

    public void setCategoryRep(CategoryRep categoryRep) {
        this.categoryRep = categoryRep;
    }

    public List<CategoryRep> getAll() {
        return this.categoryService.getAll();
    }
}
