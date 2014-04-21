
function callAjaxService(serviceCallMethodName,successHandler, errorHandler, request, serviceType, returnDataType) {
    if (request && !request.TrimEnable) {
        request = System.TrimObject(request);
    }
    serviceType = serviceType || "GET";
    request = request || {};
    returnDataType = returnDataType || "json";
    var isSpecificToUser = false;
    
    var url = serviceCallMethodNames[serviceCallMethodName].url;
    var virtaulDir = "";//getVirtualPathField();
    if (virtaulDir == null) {
        virtaulDir = "";
    }
    if (virtaulDir.length > 0) {
        while (url.indexOf("..") != -1) {
            url = url.substring(url.indexOf("..") + 2, url.length);
        }
        if (url.indexOf(virtaulDir, 0) != 0) {
            url = virtaulDir + url;
        }
    }

    if (serviceCallMethodNames[serviceCallMethodName].EnableServerCaching) {
        var cacheKeyString = JSON.stringify(request);
        if (request.UserBasedServerCachingRequired) {
            isSpecificToUser = true;
        }
        cacheKeyString = modifyCacheKey(cacheKeyString, 0, isSpecificToUser);
        //request = $.(request, { "CacheKey": cacheKeyString });
        var index = System.CacheKeys.MethodNames.find(serviceCallMethodName);
        if (!index) {
            System.CacheKeys.MethodNames.push(serviceCallMethodName);
            System.CacheKeys.Keys.push(cacheKeyString);
        }
        else if (index.length == 1) {
            System.CacheKeys.MethodNames[index[0]] = serviceCallMethodName;
            System.CacheKeys.Keys[index[0]] = cacheKeyString;
        }
    }
    if (serviceCallMethodNames[serviceCallMethodName].EnableCaching) {
        var cacheKeyString = JSON.stringify(request);
        if (request.UserBasedServerCachingRequired) {
            isSpecificToUser = true;
        }
        cacheKeyString = modifyCacheKey(cacheKeyString, 0, isSpecificToUser);
        request = $.extend(request, { "ClientCacheKey": cacheKeyString });
        if (request.PagingCriteria && request.PagingCriteria.IsRefresh && !request.IsRefresh) {
           request.IsRefresh = request.PagingCriteria.IsRefresh;
        }
        var isRefresh = isRequestForRefresh(request, 0, false);
        if (isRefresh == true) {
          clearOrUpdateCache([serviceCallMethodName]);
        }
        request = $.extend(request, { "__BypassClientAndServerCache": isRefresh, "__CacheTimeOut": serviceCallMethodNames[serviceCallMethodName].CacheTimeOut });
        System.CachingService.GetAllService.GetAll(request, serviceCallMethodName, successHandler, errorHandler,serviceType,returnDataType);

        if (!System.CacheKeyIndexor.MethodNames || System.CacheKeyIndexor.MethodNames.length == 0) {
            var item = GetLocalStorage("__CacheKeyIndexor");
            if (item) {
                item = JSON.parse(item);
                System.CacheKeyIndexor.MethodNames = item.MethodNames;
                System.CacheKeyIndexor.Keys = item.Keys;
            }
        }
        var index = 0;
        if (System.CacheKeyIndexor.MethodNames && System.CacheKeyIndexor.MethodNames.length > 0) {
        	index = System.CacheKeyIndexor.MethodNames.find(serviceCallMethodName);
        }
        if (!index) {
            System.CacheKeyIndexor.MethodNames.push(serviceCallMethodName);
            System.CacheKeyIndexor.Keys.push([cacheKeyString]);
        }
        else if (index.length > 0 && !System.CacheKeyIndexor.Keys[index[0]].find(cacheKeyString)) {
            System.CacheKeyIndexor.Keys[index[0]].push(cacheKeyString);
        }
        SetLocalStorage("__CacheKeyIndexor", JSON.stringify(System.CacheKeyIndexor));
    }
    else {
        var ajaxValue =
             {
                 "type": serviceType,
                 "url": url,
                // "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
                 "dataType": returnDataType,
                 "success": function (response) { if (serviceCallMethodNames[serviceCallMethodName].DependentMethods) { clearOrUpdateCache(serviceCallMethodNames[serviceCallMethodName].DependentMethods); } successHandler(response); },
                 "error": errorHandler,
                 "cache": true,
                 "complete": function (XMLHttpRequest, textStatus) {
                     if (textStatus == "error" && textStatus == "parseerror")
                         if (textStatus == "error") {
                         }
                 }
             };
        if (serviceType == "POST"){
        	ajaxValue.data = request;
        } 
        if ($.ajax) {
            $.ajax(ajaxValue);
        }
        else {
            jQuery.ajax(ajaxValue);
        }
    }
};

Array.prototype.find = function (searchStr) {
    var returnArray = false;
    for (var i = 0; i < this.length; i++) {
        if (typeof (searchStr) == 'function') {
            if (searchStr.test(this[i])) {
                if (!returnArray) { returnArray = []; }
                returnArray.push(i);
            }
        } else {
            if (this[i] == searchStr) {
                if (!returnArray) { returnArray = [];}
                returnArray.push(i);
            }
        }
    }
    return returnArray;
};

System.CloneObject = function (obj) {
    var newObj = (obj instanceof Array) ? [] : {};
    for (i in obj) {
        if (i == 'clone') continue;
        if (obj[i] && typeof obj[i] == "object") {
            newObj[i] = System.CloneObject(obj[i]);
        } else newObj[i] = obj[i];
    } return newObj;
};