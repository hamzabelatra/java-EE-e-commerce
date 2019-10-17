/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import cart.ShoppingCart;
import entity.Category;
import entity.Product;
import java.io.IOException;
import java.util.Collection;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import session.CategoryFacade;
import session.ProductFacade;

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
private String surcharge;
@EJB
private ProductFacade productFacade;

@Override
public void  init(ServletConfig servletConfig) throws ServletException
{
    
    super.init(servletConfig);
    surcharge = servletConfig.getServletContext().getInitParameter("deliverySurcharge");
    //store category list in the servlet    
        getServletContext().setAttribute("categories", categoryFacade.findAll());
}
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession  session = request.getSession();
         Category selectedCategory;
         Collection<Product> categoryProducts;
        String userPath= request.getServletPath();
        if(userPath.equals("/category"))
        {
            //get category id from request 
            String categoryId = request.getQueryString();
             if (categoryId != null) 
            {
                
                //get the selected category
                 selectedCategory = categoryFacade.find(Short.parseShort(categoryId));
                session.setAttribute("selectedCategory", selectedCategory);
                
                //get products of selected categories
                 categoryProducts = selectedCategory.getProductCollection();
                //place category product in the request scope 
                session.setAttribute("categoryProducts", categoryProducts);
            }
        }
             else if (userPath.equals("/viewCart")) {

            String clear = request.getParameter("clear");

            if ((clear != null) && clear.equals("true")) {

                ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
                cart.clear();
            }

            userPath = "/cart";

  }
    
        
        else if (userPath.equals("/checkout"))
        {
            ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");

            // calculate total
            cart.calculateTotal(surcharge);
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
                HttpSession session= request.getSession();
                ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
                if (userPath.equals("/addToCart"))
                {
                     //if user is adding item to the cart for the first time
                    //create cart object and attach it to user session
                    if (cart ==null)
                    {
                        cart= new ShoppingCart();
                        session.setAttribute("cart", cart);
                    }
                    //get user input from request
                    String productId = request.getParameter("productId");
                    if (!productId.isEmpty())
                    {
                        Product product =   productFacade.find(Integer.parseInt(productId));
                        cart.addItem(product);
                    }
                    userPath="/category";   
                }
                if (userPath.equals("/updateCart"))
                {
                // get input from request
            String productId = request.getParameter("productId");
            String quantity = request.getParameter("quantity");

            Product product = productFacade.find(Integer.parseInt(productId));
            cart.update(product, quantity);

            userPath = "/cart";

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

   
  

