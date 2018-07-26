package com.restaurantwebapp.restaurantwebapp.Controllers;

import com.restaurantwebapp.restaurantwebapp.Models.Menu;
import com.restaurantwebapp.restaurantwebapp.Models.MenuItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class Controllers {
    ArrayList<Menu> menus = new ArrayList<Menu>();
    HashMap<String, MenuItem> menuItems = new HashMap<String, MenuItem>();

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("menus", menus);
        model.addAttribute("menuItem", menuItems);
        return "index";
    }

    @RequestMapping(value = "/createmenu", method = RequestMethod.GET)
    public String createmenudisplay(Model model) {
        model.addAttribute("menus", menus);
        model.addAttribute("menuItem", menuItems);
        return "createmenu";
    }

    @RequestMapping(value = "/createmenuitem", method = RequestMethod.GET)
    public String createmenuitemdisplay(Model model) {
        model.addAttribute("menuItems", menuItems);
        return "createmenuitem";
    }

    @RequestMapping(value = "/createmenuitem", method = RequestMethod.POST)
    public String createmenuitemdisplay(Model model, HttpServletRequest request) {
        String name = request.getParameter("name").trim();
        if (name == "") {
            String error = "Operation failed! Named field is mandatory!";
            model.addAttribute("error", error);
            return "createmenuitem";
        }
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        float price = (request.getParameter("price") == "") ? 0.0f : (Float.parseFloat(request.getParameter("price")));
        if (menuItems.get(name) == null) {
            MenuItem menuItem = new MenuItem(price, name, description, category);
            menuItems.put(name, menuItem);
        }
        model.addAttribute("menuItems", menuItems);
        return "createmenuitem";
    }

    @RequestMapping(value = "deletemenuitem", method = RequestMethod.GET)
    public String deleteCreateMenuItem(Model model, HttpServletRequest request) {
        String name = request.getParameter("name").trim();
        if (name != "" && menuItems.get(name) != null) {
            checkAndDeleteExistingMenuItems(name);
            menuItems.remove(name);
            return "redirect:/createmenuitem";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/addmenu", method = RequestMethod.GET)
    public String addmenudisplay(Model model) {
        model.addAttribute("menus", menus);
        model.addAttribute("menuItem", menuItems);
        return "createmenu";
    }

    @RequestMapping(value = "/addmenu", method = RequestMethod.POST)
    public String processaddmenudisplay(Model model, HttpServletRequest request) {
        String restuarant = request.getParameter("restuarant").trim();
        if (restuarant == "") return "redirect:/addmenu";
        for(Menu existingmenu : menus){
            if(existingmenu.getRestuarant().equals(restuarant)){
                System.out.println("Duplicate --> restuarant name");
                model.addAttribute("menus", menus);
                return "createmenu";
            }
        }
        menus.add(new Menu(restuarant));
        model.addAttribute("menus", menus);
        model.addAttribute("menuItem", menuItems);
        return "createmenu";
    }

    @RequestMapping(value = "/addmenuitem", method = RequestMethod.GET)
    public String displayaddmenuitem(Model model) {
        model.addAttribute("menus", menus);
        model.addAttribute("menuItems", menuItems);
        return "addmenuitem";
    }

    @RequestMapping(value = "/addmenuitem", method = RequestMethod.POST)
    public String processaddmenuitem(Model model, @RequestParam(name = "menu", defaultValue = "") String menuname, @RequestParam(name = "menuitem", defaultValue = "") String menuitemname) {
        if (menuitemname != "" && menuname != "") {
            MenuItem menuItem = menuItems.get(menuitemname);
            for (Menu menu : menus) {
                if (menu.getRestuarant().equals(menuname)) {
                    for (MenuItem existingMenuItem : menu.getMenuItems()) {
                        if (existingMenuItem == menuItem) {
                            System.out.println("Exit without adding - Because, this is a duplicate entry");
                            return "redirect:/addmenuitem";
                        }
                    }
                    menu.addAnItem(menuItem);
                }
            }
        }
        return "redirect:/addmenuitem";
    }

    @RequestMapping(value = "/removeanitem", method = RequestMethod.GET)
    public String displayremovemenuitem(Model model) {
        model.addAttribute("menus", menus);
        return "removeanitem";
    }

    @RequestMapping(value = "/removeanitem", method = RequestMethod.POST)
    public String processemovemenuitem(Model model, @RequestParam(name = "menu", defaultValue = "", required = false) String menu, @RequestParam(name = "menuitem", required = false) String menuitem) {
        if (menu.equals("") && menuitem == null) {
            return "redirect:/removeanitem";
        }

        if (menu != "" && menuitem == null) {
            for (Menu menuobj : menus) {
                if (menuobj.getRestuarant().equals(menu)) {
                    model.addAttribute("menuItems", menuobj.getMenuItems());
                    model.addAttribute("menuname", menu);
                    return "removeanitem1";
                }
            }
        }

        if (!menu.equals("") && !menuitem.equals("")){
            removeMenuItem(menu,menuitem);
        }
        model.addAttribute("menus", menus);
        return "removeanitem";
    }

    public Boolean removeMenuItem(String menu, String menuItem){
        Menu myMenu;
        MenuItem myMenuItem;
        for(Menu amenu: menus){
            if(amenu.getRestuarant().equals(menu)){
                myMenu = amenu;
                for(MenuItem amenuitem:myMenu.getMenuItems()){
                    if(amenuitem.getName().equals(menuItem)){
                        myMenuItem = amenuitem;
                        int index = myMenu.getMenuItems().indexOf(myMenuItem);
                        myMenu.getMenuItems().remove(index);
                        return true;
                    }
            }
        }

        }
        return false;
    }

    public Boolean checkAndDeleteExistingMenuItems(String name){
        for (Menu menu: menus){
            int i = 0;
            for(MenuItem menuitem:menu.getMenuItems()){
                if(menuitem.getName().equals(name)){
                    menu.getMenuItems().remove(i);
                    break; //This is to exit from the inner loop once it is deleted, because if there is only one menuitem. then the list become null
                }
                i++;
            }
        }
        return true;
    }
}

