package com.ruoyi.project.system.deal.controller;

import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.JSON;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.channel.domain.SysChannel;
import com.ruoyi.project.system.channel.service.SysChannelService;
import com.ruoyi.project.system.deal.domain.SysDeal;
import com.ruoyi.project.system.deal.service.SysDealService;
import com.ruoyi.project.system.merchant.domain.SysMerchant;
import com.ruoyi.project.system.merchant.service.SysMerchantService;
import com.ruoyi.project.system.product.domain.Product;
import com.ruoyi.project.system.product.service.IProductService;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 交易表 前端控制器
 * </p>
 *
 * @author cd
 * @since 2018-05-17
 */
@Controller
@RequestMapping("/system/deal")
public class SysDealController extends BaseController {
    private String prefix = "/system/deal";
    @Autowired
    private SysDealService sysDealService;
    @Autowired
    private IUserService userService;
    @Autowired
    private SysChannelService channelService;
    @Autowired
    private SysMerchantService merchantService;
    @Autowired
    private IProductService productService;


    @RequiresPermissions("system:deal:view")
    @GetMapping()
    public String channel()
    {
        return prefix + "/deal";
    }

    @RequiresPermissions("system:deal:list")
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysDeal deal)
    {
//        setPageInfo(channel);
        List<SysDeal> list = sysDealService.selectSysDealAll();
        return getDataTable(list);
    }

    /**
     *
     * 新增交易
     */
    @RequiresPermissions("system:deal:add")
    @Log(title = "系统管理", action = "交易管理-新增交易")
    @GetMapping("/add")
    public String add(Model model)
    {
        List<User> userList = userService.selectUserAll();
        model.addAttribute("userList",userList);
        List<SysChannel> channelList = channelService.selectSysChannelAll();
        model.addAttribute("channelList",channelList);
        List<SysMerchant> merchantList = merchantService.selectSysMerchantAll();
        model.addAttribute("merchantList",merchantList);
        List<Product> productList = productService.selectProductAll();
        model.addAttribute("productList",productList);
        return prefix + "/add";
    }

    /**
     * 修改交易
     */
    @RequiresPermissions("system:deal:edit")
    @Log(title = "系统管理", action = "交易管理-修改交易")
    @GetMapping("/edit/{dealId}")
    public String edit(@PathVariable("dealId") Long dealId, Model model)
    {
        SysDeal sysDeal = sysDealService.selectSysDealById(dealId);
        model.addAttribute("sysDeal", sysDeal);
        List<User> userList = userService.selectUserAll();
        model.addAttribute("userList",userList);

        List<SysChannel> channelList = channelService.selectSysChannelAll();
        model.addAttribute("channelList",channelList);
        List<SysMerchant> merchantList = merchantService.selectSysMerchantAll();
        model.addAttribute("merchantList",merchantList);
        List<Product> productList = productService.selectProductAll();
        model.addAttribute("productList",productList);

        return prefix + "/edit";
    }
    /**
     * 保存交易
     */
    @Log(title = "系统管理", action = "交易管理-保存交易")
    @PostMapping("/save")
    @ResponseBody
    public JSON save(SysDeal sysDeal)
    {
        if (sysDealService.saveSysDeal(sysDeal) > 0)
        {
            return JSON.ok();
        }
        return JSON.error();
    }

    @RequiresPermissions("system:deal:remove")
    @Log(title = "系统管理", action = "交易管理-删除交易")
    @RequestMapping("/remove/{dealId}")
    @ResponseBody
    public JSON remove(@PathVariable("dealId") Long dealId)
    {
        SysDeal deal= sysDealService.selectSysDealById(dealId);
        if (deal == null)
        {
            return JSON.error("交易不存在");
        }
        if (sysDealService.deleteSysDealById(dealId) > 0)
        {
            return JSON.ok();
        }
        return JSON.error();
    }

    @RequiresPermissions("system:role:batchRemove")
    @Log(title = "系统管理", action = "交易管理-批量删除")
    @PostMapping("/batchRemove")
    @ResponseBody
    public JSON batchRemove(@RequestParam("ids[]") Long[] ids)
    {
        int rows = sysDealService.batchDeleteSysDeal(ids);
        if (rows > 0)
        {
            return JSON.ok();
        }
        return JSON.error();
    }

}