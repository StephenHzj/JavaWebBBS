package cn.edu.ncu.bbs.controller;

import cn.edu.ncu.bbs.domain.Item;
import cn.edu.ncu.bbs.domain.SubItem;
import cn.edu.ncu.bbs.domain.User;
import cn.edu.ncu.bbs.domain.security.MyToken;
import cn.edu.ncu.bbs.service.Impl.ItemServiceImpl;
import cn.edu.ncu.bbs.service.Impl.SubItemServiceImpl;
import cn.edu.ncu.bbs.service.Impl.UserServiceImpl;
import cn.edu.ncu.bbs.until.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/subItem")
public class SubItemController
{
    @Autowired
    private SubItemServiceImpl subItemService;

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/show")
    public String showSubItemByItemId(Model model, @RequestParam("itemId")String id)
    {
        List<SubItem> subItems = subItemService.selectByExample(id);
        Item item = itemService.selectByPrimaryKey(id);
        int count = subItemService.countByItemId(id);
        MyToken myToken = null;
        if(SecurityContextHolder.getContext().getAuthentication() instanceof MyToken)
            myToken = (MyToken) SecurityContextHolder.getContext().getAuthentication();
        if(myToken != null)
        {
            List<User> users = null;
            if(myToken.getAuthorities().contains("ROLE_ADMIN"))
            {
                users = userService.findAll();
                model.addAttribute("managers",users);
            }
            else
            {
                int manager = itemService.getManagerIdByItemId(id);
                if(manager == myToken.getUserId())
                {
                    users = userService.findAll();
                    model.addAttribute("managers",users);
                }
            }
        }
        model.addAttribute("count",count);
        model.addAttribute("selectItem",item);
        model.addAttribute("allSubItem",subItems);
        return "subItem";
    }

    @RequestMapping("/showAll")
    public String showAll(Model model)
    {
        List<SubItem> subItems = subItemService.selectAll();
        model.addAttribute("allSubItem",subItems);
        return "oldsubItem";
    }

    @ResponseBody
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public String upload(@RequestParam("subItemFile") MultipartFile file,
                         @RequestParam("subItemId") String id)
    {
//        String path = FileUtil.uploadFile(file,"subItem"+id);
        String path = FileUtil.ChangeToBase64(file);
        subItemService.setIconPath(id,path);
        return "/subItem/manage?subItemId="+id;
    }

    @RequestMapping("/create")
    public String manageSubItem(Model model,@RequestParam("itemId")String itemId)
    {
        Item item = itemService.selectByPrimaryKey(itemId);
        List<Item> items = itemService.findAll();
        model.addAttribute("oneItem",item);
        model.addAttribute("items",items);
        return "createSubItem";
    }

    @ResponseBody
    @RequestMapping("/insert")
    public String insertSubItem(@RequestParam("subItemName")String name,
                                @RequestParam("subItemDescription")String description,
                                @RequestParam(value = "manager",defaultValue = "") String manager,
                                @RequestParam(value = "itemId",defaultValue = "")String itemId)
    {
        subItemService.insert(name, description, manager, itemId);
        return "/subItem/show?itemId="+itemId;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public String deleteSubItem(@RequestParam("subItemId")String id)
    {
        Integer itemId = subItemService.selectByPrimaryKey(id).getItemId();
        subItemService.delete(id);
        return "/subItem/show?itemId="+itemId;
    }

    @ResponseBody
    @RequestMapping("/update")
    public String update(@RequestParam("subItemId")String id,
                       @RequestParam(value = "subItemName",defaultValue = "")String name,
                       @RequestParam(value = "subItemDescription",defaultValue = "")String description,
                       @RequestParam(value = "manager",defaultValue = "") String manager,
                       @RequestParam(value = "itemId",defaultValue = "")String itemId)
    {
        subItemService.update(id, name, description, manager, itemId);
        return "/subItem/show?itemId="+itemId;
    }

    @ResponseBody
    @RequestMapping("/getItem")
    public String getItem(Model model,@RequestParam("itemId")String id)
    {
        model.addAttribute("belongItem",itemService.selectByPrimaryKey(id));
        return null;
    }
}
