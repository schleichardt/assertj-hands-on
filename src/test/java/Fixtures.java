import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.json.SphereJsonUtils;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.List;

public class Fixtures {
    public static List<ProductProjection> productProjectionList() {
        return productProjectionPagedQueryResult().getResults();
    }

    public static PagedQueryResult<ProductProjection> productProjectionPagedQueryResult() {
        final String jsonString;
        try {
            jsonString = ResourceUtil.stringFromResource("product-projection-paged-query-result.json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return SphereJsonUtils.readObject(jsonString, new TypeReference<PagedQueryResult<ProductProjection>>() {
        });
    }
}
