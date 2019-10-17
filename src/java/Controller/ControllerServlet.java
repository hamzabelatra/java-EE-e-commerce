/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import entity.Category;
import entity.Product;
import java.io.IOException;
import java.util.Collection;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import session.CategoryFacade;

/**
 *
 * @author Hamza
 */
@WebServlet(name = "ControllerServlet", 
            loadOnStartup = 1,
        urlPatterns = {"/category",
                        "/addToCart",
                        "/viewCart",
                        "/updateCart",
                        "/checkout",
                        "/purchase",
                        "/chooseLanguage"
})
public class ControllerServlet extends HttpServlet {

@EJB
private CategoryFacade categoryFacade;

@Override
public void  init() throws ServletException
{
    //store category list in the servlet    
        getServletContext().setAttribute("categories", categoryFacade.findAll());
}
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userPath= request.getServletPath();
        if(userPath.equals("/category"))
        {
            //get category id from request 
            String categoryId = request.getQueryString();
             if (categoryId != null) 
            {
                
                //get the selected category
                Category selectedCategory = categoryFacade.find(Short.parseShort(categoryId));
                request.setAttribute("selectedCategory", selectedCategory);
                
                //get products of selected categories
                Collection<Product> categoryproducts = selectedCategory.getProductCollection();
                //place category product in the request scope 
                request.setAttribute("categoryProducts", categoryproducts);
            }
        }
        else if (userPath.equals("/viewCart"))
        {
            userPath="/cart";
        }
        
        else if (userPath.equals("/checkout"))
        {
            //implement checkout page request
        }
        else if (userPath.equals("chooseLanguage"))
        {
            //implement language request
        }
        //use request dispatch to forward  request internally
        String url="/WEB-INF/view"+ userPath + ".jsp";
        try{
            request.getRequestDispatcher(url).forward(request, response);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
                String userPath= request.getServletPath();

                if (userPath.equals("/addToCart"))
                {
                    //implement add product to cart
                }
                if (userPath.equals("/updateCart"))
                {
                    //implement update cart action
                }
                if(userPath.equals("/purchase"))
                {
                    //implement purchase action 
                    userPath="/confirmation";
                }
                   String url="/WEB-INF/view"+ userPath + ".jsp";
        try{
            request.getRequestDispatcher(url).forward(request, response);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    }

   
  

