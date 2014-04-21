var System = {};
var clientCacheTimeOut = 86400;  //In secs
System.CacheKeys = { "MethodNames": [], "Keys": [] };
System.CacheKeyIndexor = { "MethodNames": [], Keys: [] };
var currentVersionForLocalStorage = "4.7.0.0";
System.TrimObject = function (obj) {
    var type = $.type(obj);
    if (type != "object") {
        return obj;
    }
    $.each(obj, function (i, value) {
        if ($.type(value) == "string") {
            obj[i] = value.trim();
        }
        else if ($.type(value) == "object") {
            obj[i] = System.TrimObject(obj[i]);
        }
    });
    return obj;
};

System.IsLocalStorageAvailable = false;

try {
    if (localStorage) {
        System.IsLocalStorageAvailable = true;
    }
    else {
        System.IsLocalStorageAvailable = false;
        clientCacheTimeOut = 720;
    }
}
catch (ex) {
    System.IsLocalStorageAvailable = false;
    clientCacheTimeOut = 720;
}

System.CachingService = new Services();

function Services() {
    this.GetAllService = new GetAllService();
    this.ErrorHandling = true;
    //if (applicationMode == "Debug") {
    //    this.ErrorHandling = false;
    //}

    this.CallService = function (serviceCallMethodName, request, successHandler, errorHandler,serviceType, dataType ) {
        var returnDataType = "json";
        if (dataType) {
            returnDataType = dataType;
        }
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
        var ajaxValue =
        {
            "type": serviceType,
            "url": url,
           // "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
            headers: { 
                Accept : "text/csv,*/*"
            },
            "dataType": returnDataType,
            "success": successHandler,
            "error": errorHandler,
            "complete": function (XMLHttpRequest, textStatus) {
                if (textStatus == "error" && textStatus == "parseerror") { this; }
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
    };

    this.ClearCache = function (methodName) {
        if (methodName == "GetCatalogValues") {
            this.CatalogService.CatalogMastersWithCatalogValues = [];
            this.CatalogService.CurrentCatalogs = [];
            if (System.IsLocalStorageAvailable && localStorage) {
                var index = System.CacheKeyIndexor.MethodNames.find(methodName);
                if (index && index.length > 0) {
                    var keys = System.CacheKeyIndexor.Keys[index[0]];
                    if (keys) {
                        for (var i = 0; i < System.CacheKeyIndexor.Keys[index[0]].length; i++) {
                            localStorage.removeItem(System.CacheKeyIndexor.Keys[index[0]][i]);
                            System.CacheKeyIndexor.Keys[index[0]].splice(i, 1);
                            i--;
                        }
                    }
                }
            }
        }
        else if (this.GetAllService.GetAlls) {
            for (var i = 0; i < this.GetAllService.GetAlls.length; i++) {
                if (this.GetAllService.GetAllsIndexer[i].ServiceCallMethodName && this.GetAllService.GetAllsIndexer[i].ServiceCallMethodName == methodName) {
                    this.GetAllService.GetAllsIndexer.splice(i, 1);
                    this.GetAllService.GetAlls.splice(i, 1);
                    i--;
                }
            }
            if (System.IsLocalStorageAvailable && localStorage) {
                var index = System.CacheKeyIndexor.MethodNames.find(methodName);
                if (index && index.length > 0) {
                    var keys = System.CacheKeyIndexor.Keys[index[0]];
                    if (keys) {
                        for (var i = 0; i < System.CacheKeyIndexor.Keys[index[0]].length; i++) {
                            localStorage.removeItem(System.CacheKeyIndexor.Keys[index[0]][i]);
                            System.CacheKeyIndexor.Keys[index[0]].splice(i, 1);
                            i--;
                        }
                    }
                }
            }
        }
    };
}

function GetAllService() {
    this.GetAlls = null;
    this.GetAllsIndexer = null;
    this.SuccessHandler = null;
    this.ErrorHandler = null;
    this.CurrentRequest = null;
    this.CurrentMethodName = null;
    this.ExecutionAlreadyInProgress = false;

    /*
    Used for maintaining the queue
    */
    var SuccessHandlerQueue = new Array();
    var ErrorHandlerQueue = new Array();
    var GetAllsRequestQueue = new Array();
    var GetAllsMethodNameQueue = new Array();
    var serviceCallType = "POST";
    var serviceCallReturnType = "json";
    function callAllsServiceSuccess(response, request) {
        if (response && (response.IsException == true || response.IsFailure == true)) {
            if (System.CachingService.ErrorHandling) {
                try { System.CachingService.GetAllService.SuccessHandler(response); } catch (ex) { }
            }
            else {
                System.CachingService.GetAllService.SuccessHandler(response);
            }

            GetAllsRequestQueue.splice(0, 1);
            GetAllsMethodNameQueue.splice(0, 1);
            SuccessHandlerQueue.splice(0, 1);
            ErrorHandlerQueue.splice(0, 1);
            System.CachingService.GetAllService.ExecutionAlreadyInProgress = false;
        }
        else {
            var valueIsPresent = false;
            if (request.__BypassClientAndServerCache) {
                valueIsPresent = contains(request, System.CachingService.GetAllService.GetAllsIndexer, true);
            }
            if (response && request.__BypassClientAndServerCache && !valueIsPresent) {
                System.CachingService.GetAllService.GetAlls.push(response);
                System.CachingService.GetAllService.GetAllsIndexer.push(request);
                SetLocalStorage(request.ClientCacheKey, JSON.stringify(response));
                //localStorage.setItem(request.ClientCacheKey, JSON.stringify(response));
            }
            else if (response && !request.__BypassClientAndServerCache) {
                System.CachingService.GetAllService.GetAlls.push(response);
                System.CachingService.GetAllService.GetAllsIndexer.push(request);
                SetLocalStorage(request.ClientCacheKey, JSON.stringify(response));
                //localStorage.setItem(request.ClientCacheKey, JSON.stringify(response));
            }
            returnGetAlls();
        }
    }

    function callGetAllsServiceError(response) {
        if (System.CachingService.GetAllService.ErrorHandler) {
            if (System.CachingService.ErrorHandling) {
                try { System.CachingService.GetAllService.ErrorHandler(response); } catch (ex) { }
            }
            else {
                System.CachingService.GetAllService.ErrorHandler(response);
            }
        }
        GetAllsRequestQueue.splice(0, 1);
        GetAllsMethodNameQueue.splice(0, 1);
        SuccessHandlerQueue.splice(0, 1);
        ErrorHandlerQueue.splice(0, 1);
        System.CachingService.GetAllService.ExecutionAlreadyInProgress = false;
    }

    this.GetAll = function (request, methodName, successHandler, errorHandler,serviceType,returnDataType) {
        //request.ServiceCallMethodName = methodName;
        GetAllsRequestQueue.push(request);
        GetAllsMethodNameQueue.push(methodName);
        SuccessHandlerQueue.push(successHandler);
        ErrorHandlerQueue.push(errorHandler);
        if (System.CachingService.GetAllService.GetAlls == null) {
            System.CachingService.GetAllService.GetAlls = new Array();
            System.CachingService.GetAllService.GetAllsIndexer = new Array();
        }
        serviceCallType = serviceType;
        serviceCallReturnType = returnDataType;
        schedule();
        
    };

    function dispatch() {
        System.CachingService.GetAllService.ExecutionAlreadyInProgress = true;
        var request = GetAllsRequestQueue[0];
        System.CachingService.GetAllService.CurrentMethodName = GetAllsMethodNameQueue[0];
        System.CachingService.GetAllService.CurrentRequest = request;
        System.CachingService.GetAllService.SuccessHandler = SuccessHandlerQueue[0];
        System.CachingService.GetAllService.ErrorHandler = ErrorHandlerQueue[0];
        var serviceCallRequired = true;

        if (request) {
            if (contains(request, System.CachingService.GetAllService.GetAllsIndexer)) {
                serviceCallRequired = false;
            }
        }
        if (serviceCallRequired) {
            System.CachingService.CallService(System.CachingService.GetAllService.CurrentMethodName, request, function (response) { callAllsServiceSuccess(response, request); }, callGetAllsServiceError,serviceCallType,serviceCallReturnType);
        }
        else {
            setTimeout(returnGetAlls, 0);
        }
    }

    function schedule() {
        if (GetAllsRequestQueue.length > 0 && !System.CachingService.GetAllService.ExecutionAlreadyInProgress) {
            dispatch();
        }
        else if (GetAllsRequestQueue.length > 0) {
            wait();
        }
    }

    function wait() {
        if (!System.CachingService.GetAllService.ExecutionAlreadyInProgress) {
            schedule();
        }
        if (GetAllsRequestQueue.length > 0) {
            setTimeout(wait, 100);
        }
    }

    function returnGetAlls() {
        var itemFound = false;
        for (var i = 0; i < System.CachingService.GetAllService.GetAlls.length; i++) {
            if (System.CachingService.GetAllService.CurrentRequest.ClientCacheKey == System.CachingService.GetAllService.GetAllsIndexer[i].ClientCacheKey) {
                if (System.CachingService.GetAllService.SuccessHandler) {
                    if (System.CachingService.ErrorHandling) {
                        var cloneObj = System.CloneObject(System.CachingService.GetAllService.GetAlls[i]);
                        try { System.CachingService.GetAllService.SuccessHandler(cloneObj); itemFound = true; } catch (ex) { }
                    }
                    else {
                        var cloneObj = System.CloneObject(System.CachingService.GetAllService.GetAlls[i]);
                        System.CachingService.GetAllService.SuccessHandler(cloneObj);
                        itemFound = true;
                    }
                }
                break;
            }
        }

        if (!itemFound && System.IsLocalStorageAvailable) {
            var item = localStorage.getItem(System.CachingService.GetAllService.CurrentRequest.ClientCacheKey);
            System.CachingService.GetAllService.SuccessHandler(JSON.parse(item));
        }

        GetAllsRequestQueue.splice(0, 1);
        GetAllsMethodNameQueue.splice(0, 1);
        SuccessHandlerQueue.splice(0, 1);
        ErrorHandlerQueue.splice(0, 1);
        System.CachingService.GetAllService.ExecutionAlreadyInProgress = false;
    }

    function contains(request, requests, overrideDefaultCheck) {
        var index = -1;
        if (requests) {
            for (var i = 0; i < requests.length; i++) {
                if (requests[i].ClientCacheKey == request.ClientCacheKey) {
                    index = i;
                    break;
                }
            }
        }
        if (index > -1 && !overrideDefaultCheck && request.__BypassClientAndServerCache) {
            removeCache(index);
            return false;
        }
        var present = false;
        if (requests && index > -1) {
            var timeOut = hasTimedOut(index);
            if (timeOut) {
                present = false;
                removeCache(index);
            }
            else {
                present = true;
            }
        }
        if (System.IsLocalStorageAvailable && localStorage && present == false) {
            if (!overrideDefaultCheck && request.__BypassClientAndServerCache) {
                return false;
            }
            if (localStorage.getItem(request.ClientCacheKey)) {
                var timeOut = hasTimedOut(-1, request.ClientCacheKey);
                if (timeOut) {
                    present = false;
                    removeCache(-1);
                }
                else {
                    present = true;
                }
            }
        }
        return present;
    }

    function hasTimedOut(index, clientCacheKey) {
        if (index != -1) {
            if (!System.CachingService.GetAllService.GetAllsIndexer[index].__CallTimeIn) {
                System.CachingService.GetAllService.GetAllsIndexer[index].__CallTimeIn = new Date();
                return false;
            }
        }
        var item = null;
        var dateTimeCall = null;
        if (index == -1 && clientCacheKey) {
            if (System.IsLocalStorageAvailable && localStorage) {
                item = localStorage.getItem("__CallTimeIn__" + clientCacheKey);
                if (!item) {
                    return false;
                }
                else {
                    dateTimeCall = (new Date(item)).getTime();
                }
            }
            else {
                return false;
            }
        }
        else {
            dateTimeCall = System.CachingService.GetAllService.GetAllsIndexer[index].__CallTimeIn.getTime();
        }
        var dateTimeNow = new Date();
        var difference = dateTimeNow.getTime() - dateTimeCall;
        difference = difference / 1000;
        var cacheTimeOut;
        if (index != -1) {
            cacheTimeOut = System.CachingService.GetAllService.GetAllsIndexer[index].__CacheTimeOut;
        }
        else {
            cacheTimeOut = clientCacheTimeOut;
        }
        if (cacheTimeOut) {
            if (cacheTimeOut >= 0 && difference > cacheTimeOut) {
                return true;
            }
        }
        else if (clientCacheTimeOut >= 0 && difference > clientCacheTimeOut) {
            return true;
        }
        return false;
    }

    function removeCache(index) {
        if (index != -1) {
            System.CachingService.GetAllService.GetAlls.splice(index, 1);
            System.CachingService.GetAllService.GetAllsIndexer.splice(index, 1);
        }
        if (System.IsLocalStorageAvailable && localStorage) {
            localStorage.removeItem(System.CachingService.GetAllService.CurrentRequest.ClientCacheKey);
        }
    }
}

function modifyCacheKey(requestObj, count, isSpecificToUser) {
    if ($.type(requestObj) == "string") {
        requestObj = JSON.parse(requestObj);
    }
    $.each(requestObj, function (i, value) {
        switch ($.type(value)) {
            case "object":
                modifyCacheKey(value, count, isSpecificToUser);
                break;
            case "string":
                if (i == "UserId" && count < 2 && !isSpecificToUser) {
                    requestObj[i] = null;
                    count++;
                }
                break;
            case "boolean":
                if (i == "IsRefresh" && count < 2) {
                    requestObj[i] = false;
                    count++;
                }
                break;
        }
        if (count == 2) {
            return JSON.stringify(requestObj);
        }
    });
    return JSON.stringify(requestObj);
}

function isRequestForRefresh(requestObj, count, isRefresh) {
    if ($.type(requestObj) == "string") {
        requestObj = JSON.parse(requestObj);
    }
    $.each(requestObj, function (i, value) {
        switch ($.type(value)) {
            case "object":
                if (requestObj && (requestObj.IsRefresh == false || requestObj.IsRefresh == true)) {
                    isRefresh = requestObj.IsRefresh;
                }
                else {
                    isRefresh = isRequestForRefresh(value, count, isRefresh);
                }
                return isRefresh;
                break;
            case "boolean":
                if (i == "IsRefresh" && count < 3) {
                    isRefresh = requestObj[i];
                    return isRefresh;
                    count++;
                }
                break;
        }
        if (count == 3) {
            return isRefresh;
        }
    });
    return isRefresh;
}

function clearOrUpdateCache(methodNames) {
    if (methodNames) {
        for (var i = 0; i < methodNames.length; i++) {
            if (methodNames[i] == "GetCatalogValues") {
                System.CachingService.ClearCache(methodNames[i]);
            }
            else if (serviceCallMethodNames[methodNames[i]] && serviceCallMethodNames[methodNames[i]].EnableCaching) {
                System.CachingService.ClearCache(methodNames[i]);
            }
        }
    }
}

var SetLocalStorage = function (key, value) {
    if (System.IsLocalStorageAvailable && localStorage) {
        try {
            localStorage.setItem(key, value);
            localStorage.setItem("__CallTimeIn__" + key, Date());
        }
        catch (ex) {
            if (ex.code == 22) {
                if (localStorage.clear) {
                    localStorage.clear();
                }
                else {
                    for (var i = 0; i < localStorage.length; i++) {
                        localStorage.removeItem(localStorage.key(i));
                    }
                }
                System.CacheKeyIndexor.MethodNames = [];
                System.CacheKeyIndexor.Keys = [];
            }
        }
    }
};
// To Set an item in localStorage - end

// To Get an item from localStorage - start
var GetLocalStorage = function (key) {
    var item = null;
    if (System.IsLocalStorageAvailable && localStorage) {
        try {
            item = localStorage.getItem(key);
        }
        catch (ex) {
        }
    }
    return item;
};
// To Get an item from localStorage - end

// To Update localStorage - start
var UpdateLocalStorageKeys = function (dataChangeInfo) {
    if (!System.IsLocalStorageAvailable || !localStorage) {
        return;
    }
    if (!System.CacheKeyIndexor.MethodNames || System.CacheKeyIndexor.MethodNames.length == 0) {
        var item = GetLocalStorage("__CacheKeyIndexor");
        if (item) {
            item = JSON.parse(item);
            System.CacheKeyIndexor.MethodNames = item.MethodNames;
            System.CacheKeyIndexor.Keys = item.Keys;
        }
    }
    if (dataChangeInfo && dataChangeInfo.length > 0 && System.MethodNameIndexor) {
        if (System.CacheKeyIndexor.MethodNames && System.CacheKeyIndexor.MethodNames.length > 0) {
            for (var j = 0; j < dataChangeInfo.length; j++) {
                var clientMethodName = System.MethodNameIndexor[dataChangeInfo[j].MethodName];
                if (clientMethodName) {
                    dataChangeInfo[j].MethodName = clientMethodName;
                }
                for (var methodIndex = 0; methodIndex < dataChangeInfo[j].MethodName.length; methodIndex++) {
                    var index = System.CacheKeyIndexor.MethodNames.find(dataChangeInfo[j].MethodName[methodIndex]);
                    if (index && index.length > 0) {
                        var keys = System.CacheKeyIndexor.Keys[index[0]];
                        if (keys) {
                            var itemRemoved = false;
                            var serverModifiedDateTime = dataChangeInfo[j].UpdatedDateTime;
                            for (var i = 0; i < System.CacheKeyIndexor.Keys[index[0]].length; i++) {
                                var clientStorageDateTime = GetLocalStorage("__CallTimeIn__" + System.CacheKeyIndexor.Keys[index[0]][i]);
                                if (serverModifiedDateTime && clientStorageDateTime) {
                                    if (((new Date(serverModifiedDateTime)) - (new Date(clientStorageDateTime))) > (System.ServerDateTime - System.ClientDateTime)) {
                                        localStorage.removeItem(System.CacheKeyIndexor.Keys[index[0]][i]);
                                        itemRemoved = true;
                                        System.CacheKeyIndexor.Keys[index[0]].splice(i, 1);
                                        i--;
                                    }
                                }
                                else {
                                    localStorage.removeItem(System.CacheKeyIndexor.Keys[index[0]][i]);
                                    System.CacheKeyIndexor.Keys[index[0]].splice(i, 1);
                                    i--;
                                    itemRemoved = true;
                                }
                            }
                            if (itemRemoved) {
                                clearOrUpdateCache([dataChangeInfo[j].MethodName[methodIndex]]);
                                SetLocalStorage("__CacheKeyIndexor", JSON.stringify(System.CacheKeyIndexor));
                            }
                        }
                    }
                }
            }
        }
    }
    setTimeout(UpdateLocalStorageCall, 480000); // Repeat every 8 mins(480000)
};
// To Update localStorage - end

var ClearLocalStorageAndCache = function (showMessage) {
    if (System.IsLocalStorageAvailable && localStorage) {
        localStorage.clear();
    }
    System.CachingService = new Services();
    System.CacheKeyIndexor = { "MethodNames": [], Keys: [] };
    if (showMessage) {
        SuccessMessageHandler("Local storage and cache cleared");
    }
};

var ValidateLocalStorageForVersion = function () {
    var versionInLocalStorage = GetLocalStorage("CurrentVersionForLocalStorage");
    if (versionInLocalStorage && currentVersionForLocalStorage != versionInLocalStorage) {
        ClearLocalStorageAndCache(false);
    }
    SetLocalStorage("CurrentVersionForLocalStorage", currentVersionForLocalStorage);
};
if (System.IsLocalStorageAvailable) {
    ValidateLocalStorageForVersion();
}

var UpdateLocalStorageCall = function () {
    var callBack = function (response) {
        if (response != null) {
            if (response[0].Response != null && response[0].Response.IsException == false) {
                System.ServerDateTime = new Date(response[0].Response.ServerDateTime);
                System.ClientDateTime = new Date();
                UpdateLocalStorageKeys(response[0].Response.DataChangeInfo);
            }
        }
    };
    var TenantId = readCookie('TenantId');
    var requestG = { Request: { TenantId: TenantId }, ServiceCallMethodName: "GetDataChangeInfo" };
    var ascm = new AsynchronousServiceCallManager(callBack);
    ascm.AddService(requestG);
    ascm.Execute();
};
//{"DataChangeInfo":[{"MethodName":"getE2EMF","UpdatedDateTime":"Fri, 11 Apr 2014 10:14:45 GMT"}],"IsException":false,"ServerDateTime":"Fri, 11 Apr 2014 10:21:44 GMT"}