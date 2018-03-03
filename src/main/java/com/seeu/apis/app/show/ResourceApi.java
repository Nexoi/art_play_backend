package com.seeu.apis.app.show;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 这玩意可视作：资源组、资源的综合操作
 */

@Api(tags = "展览资源信息", description = "展览资源／通过BeaconUUID访问资源组／通过二维码URL访问资源组等")
@RestController
@RequestMapping("/api/v1/show/resources")
public class ResourceApi {

}
