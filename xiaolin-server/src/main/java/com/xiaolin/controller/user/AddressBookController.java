package com.xiaolin.controller.user;

import com.xiaolin.dto.AddressBookDTO;
import com.xiaolin.result.Result;
import com.xiaolin.service.AddressBookService;
import com.xiaolin.vo.AddressBookVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lzh
 * @description: 地址簿控制器
 * @date 2025/11/28 16:45
 */
@RestController
@RequestMapping("/user/addressBook")
@RequiredArgsConstructor
public class AddressBookController {

    private final AddressBookService addressBookService;

    /**
     * 新增
     * @param form
     * @return
     */
    @PostMapping
    public Result<Integer> save(@RequestBody AddressBookDTO form) {
        return addressBookService.save(form);
    }

    /**
     * 全部地址
     * @return
     */
    @GetMapping("/list")
    public Result<List<AddressBookVO>> list() {
        return addressBookService.listAll();
    }

    /**
     * 默认地址
     * @return
     */
    @GetMapping("/default")
    public Result<AddressBookVO> getDefaultAddress() {
        return addressBookService.getDefaultAddress();
    }

    /**
     * 更新
     * @param form
     * @return
     */
    @PutMapping
    public Result<Integer> update(@RequestBody AddressBookDTO form) {
        if (form.getId() == null){
            return Result.error("更新哪个地址呀");
        }
        return addressBookService.update(form);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping
    public Result<Integer> remove(Long id) {
        if (id == null){
            return Result.error("删除哪个地址呀");
        }
        return addressBookService.remove(id);
    }

    /**
     * 详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<AddressBookVO> getAddress(@PathVariable Long id) {
        if (id == null){
            return Result.error("查看哪个地址呀");
        }
        return addressBookService.getAddressById(id);
    }

    /**
     * 设置默认地址
     * @param id
     * @return
     */
    @PutMapping("/default")
    public Result<Integer> setDefAddr(@RequestBody AddressBookDTO form) {
        if (form.getId() == null){
            return Result.error("设置哪个地址为默认地址呀");
        }
        return addressBookService.setDefAddrById(form.getId());
    }
}
