package com.lms.sqlfather.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.lms.lmscommon.model.dto.interfaceInfo.AddInterfaceInfoRequest;
import com.lms.lmscommon.model.dto.interfaceInfo.PageInterfaceInfoRequest;
import com.lms.lmscommon.model.dto.interfaceInfo.UpdateInterfaceInfoRequest;
import com.lms.lmscommon.model.entity.InterfaceInfo;
import com.lms.lmscommon.model.vo.interfaceInfo.InterfaceInfoVO;


import java.util.List;


/**
 *
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {


     /**
      *
      * @param addInterfaceInfoDto
      * @param uid
      * @return
      */
     Boolean saveInterface(AddInterfaceInfoRequest addInterfaceInfoDto, Long uid);

     /**
      *
      * @param updateInterfaceInfoDto
      * @return
      */
     Boolean updateInterface(UpdateInterfaceInfoRequest updateInterfaceInfoDto);

     /**
      *
      * @param ids
      * @return
      */
     Boolean deleteInterface(List<Long> ids);

     /**
      *
      * @param pageInterfaceInfoDto
      * @return
      */
     Page<InterfaceInfoVO>  pageInterfaceVo(PageInterfaceInfoRequest pageInterfaceInfoDto);

     /**
      *
      * @param id
      * @return
      */
     InterfaceInfoVO getInterfaceById(Long id);

     /**
      *
      * @param id
      * @return
      */
     Boolean onlineInterface(Long id);

     /**
      *
      * @param id
      * @return
      */
     Boolean offlineInterface(Long id);



}
