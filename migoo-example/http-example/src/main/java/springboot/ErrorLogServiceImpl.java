package springboot;

import org.springframework.stereotype.Service;
import xyz.migoo.framework.apilog.core.ApiErrorLog;
import xyz.migoo.framework.apilog.core.ApiErrorLogFrameworkService;

@Service
public class ErrorLogServiceImpl implements ApiErrorLogFrameworkService {
    @Override
    public void createApiErrorLog(ApiErrorLog apiErrorLog) {

    }
}
