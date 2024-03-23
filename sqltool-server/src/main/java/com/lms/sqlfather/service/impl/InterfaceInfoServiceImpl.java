package com.lms.sqlfather.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lms.exception.BusinessException;
import com.lms.lmscommon.constant.StatusConstant;
import com.lms.lmscommon.model.dto.interfaceInfo.AddInterfaceInfoRequest;
import com.lms.lmscommon.model.dto.interfaceInfo.PageInterfaceInfoRequest;
import com.lms.lmscommon.model.dto.interfaceInfo.UpdateInterfaceInfoRequest;
import com.lms.lmscommon.model.entity.InterfaceInfo;
import com.lms.lmscommon.model.enums.InterfaceInfoStatusEnum;
import com.lms.lmscommon.model.enums.MethodEnum;

import com.lms.lmscommon.model.vo.interfaceInfo.InterfaceInfoVO;
import com.lms.lmscommon.utils.MybatisUtils;
import com.lms.sqlfather.mapper.InterfaceInfoMapper;

import com.lms.sqlfather.service.InterfaceInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.lms.lmscommon.model.factory.InterfaceInfoFactory.INTERFACE_CONVERTER;


@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements InterfaceInfoService {





    @Override
    public Boolean saveInterface(AddInterfaceInfoRequest addInterfaceInfoDto, Long uid) {

        //校验接口名是否重复
        String name = addInterfaceInfoDto.getName();
        Integer method = addInterfaceInfoDto.getMethod();
        Integer status = addInterfaceInfoDto.getStatus();
        BusinessException.throwIf(MybatisUtils.existCheck(this, Map.of("name",name)));
        BusinessException.throwIfNot(validMethod(method));
        BusinessException.throwIfNot(validStatus(status));
        InterfaceInfo interfaceInfo=new InterfaceInfo();
        BeanUtils.copyProperties(addInterfaceInfoDto,interfaceInfo);
        interfaceInfo.setUserId(uid);

        return this.save(interfaceInfo);
    }

    @Override
    public Boolean updateInterface(UpdateInterfaceInfoRequest updateInterfaceInfoDto) {

        //校验id是否存在
        Integer method = updateInterfaceInfoDto.getMethod();
        Integer status = updateInterfaceInfoDto.getStatus();
        BusinessException.throwIfNot(MybatisUtils.existCheck(this,Map.of("id",updateInterfaceInfoDto.getId())));
        BusinessException.throwIfNot(validMethod(method));
        BusinessException.throwIfNot(validStatus(status) );
        InterfaceInfo interfaceInfo=new InterfaceInfo();
        BeanUtils.copyProperties(updateInterfaceInfoDto,interfaceInfo);
        return this.updateById(interfaceInfo);
    }

    @Override
    public Boolean deleteInterface(List<Long> ids) {
         if(ObjectUtils.isEmpty(ids)) {
             return false;
         }
        return this.update(new UpdateWrapper<InterfaceInfo>().set("delete_flag", StatusConstant.DELETED).in("id",ids));
    }

    @Override
    public Page<InterfaceInfoVO> pageInterfaceVo(PageInterfaceInfoRequest pageInterfaceInfoDto) {
        Integer method = pageInterfaceInfoDto.getMethod();
        Integer status = pageInterfaceInfoDto.getStatus();
        String name = pageInterfaceInfoDto.getName();
        Integer pageNum = pageInterfaceInfoDto.getPageNum();
        Integer pageSize = pageInterfaceInfoDto.getPageSize();
        Page<InterfaceInfo> page = this.page(new Page<>(pageNum, pageSize), new QueryWrapper<InterfaceInfo>().like(!StringUtils.isEmpty(name), "name", name)
                .eq(validMethod(method), "method", method).eq(validStatus(status), "status", status));

        List<InterfaceInfo> records = page.getRecords();

        List<InterfaceInfoVO> interfaceInfoVos = INTERFACE_CONVERTER.toListInterfaceVO(records);
        Page<InterfaceInfoVO> interfaceInfoVoPage=new Page<>(pageNum,pageSize,page.getTotal());
        interfaceInfoVoPage.setRecords(interfaceInfoVos);
        return interfaceInfoVoPage;
    }

    @Override
    public InterfaceInfoVO getInterfaceById(Long id) {

        InterfaceInfo byId = this.getById(id);
        return INTERFACE_CONVERTER.toInterfaceVO(byId);
    }

    @Override
    public Boolean onlineInterface(Long id) {
        return this.update(new UpdateWrapper<InterfaceInfo>().eq("id",id).set("status",InterfaceInfoStatusEnum.ONLINE.getValue()));
    }
    @Override
    public Boolean offlineInterface(Long id) {
        return this.update(new UpdateWrapper<InterfaceInfo>().eq("id",id).set("status",InterfaceInfoStatusEnum.OFFLINE.getValue()));
    }

    private boolean validMethod(Integer method){
        List<Integer> values = MethodEnum.getValues();
        return !ObjectUtils.isEmpty(method)&&values.contains(method);
    }

    private boolean validStatus(Integer status){
        List<Integer> values = InterfaceInfoStatusEnum.getValues();
        return  !ObjectUtils.isEmpty(status)&&values.contains(status);
    }
}
