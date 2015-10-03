package assertjtest;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.ProductProjection;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class ListAssertionsTest {
    /*
    includes stuff from iterable
     */

    private static final List<ProductProjection> list = Fixtures.productProjectionList();

    @Test
    public void name1() {
        final List<String> names = list.stream().map(p -> p.getId()).collect(toList());
        assertThat(names).isEqualTo(asList("foo", "bar", "347caac1-daa2-4e31-bc95-e237ba1e2b91"));
/*
org.junit.ComparisonFailure: expected:<["[foo", "bar", "347caac1-daa2-4e31-bc95-e237ba1e2b91]"]> but was:<["[8665d91a-eab0-45a8-a378-e6603d66d084",
"347caac1-daa2-4e31-bc95-e237ba1e2b91",
"f7edccd2-95d7-4fea-b511-ae5b3e309c7d",
"ff0c34c3-bd33-4991-89f5-dfa96b69b26c",
"461e2e52-50c9-4d77-a2f2-1cb77b601556]"]>
 */
    }

    @Test
    public void name2() {
        final List<String> names = list.stream().map(p -> p.getId()).collect(toList());
        assertThat(names).contains("foo", "bar", "347caac1-daa2-4e31-bc95-e237ba1e2b91");
/*
 <["8665d91a-eab0-45a8-a378-e6603d66d084",
    "347caac1-daa2-4e31-bc95-e237ba1e2b91",
    "f7edccd2-95d7-4fea-b511-ae5b3e309c7d",
    "ff0c34c3-bd33-4991-89f5-dfa96b69b26c",
    "461e2e52-50c9-4d77-a2f2-1cb77b601556"]>
to contain:
 <["foo", "bar", "347caac1-daa2-4e31-bc95-e237ba1e2b91"]>
but could not find:
 <["foo", "bar"]>
 */
    }

    @Test
    public void name3() {
        assertThat(list).extracting("id").contains("foo", "bar", "347caac1-daa2-4e31-bc95-e237ba1e2b91");
/*
 <["8665d91a-eab0-45a8-a378-e6603d66d084",
    "347caac1-daa2-4e31-bc95-e237ba1e2b91",
    "f7edccd2-95d7-4fea-b511-ae5b3e309c7d",
    "ff0c34c3-bd33-4991-89f5-dfa96b69b26c",
    "461e2e52-50c9-4d77-a2f2-1cb77b601556"]>
to contain:
 <["foo", "bar", "347caac1-daa2-4e31-bc95-e237ba1e2b91"]>
but could not find:
 <["foo", "bar"]>
 */
    }

    @Test
    public void categories1() {
        final Reference<Category> longSleevesCategory = Reference.of(Category.referenceTypeId(), "eab84062-f4d7-4dd6-a576-ce5c10923e68");
        assertThat(list)
                .hasSize(5)
                .contains(list.get(0))
                .filteredOn(productProjection -> productProjection.getCategories().contains(longSleevesCategory))
                .extracting("id")
                .contains("foo");
/*
java.lang.AssertionError:
Expecting:
  <["8665d91a-eab0-45a8-a378-e6603d66d084",
    "ff0c34c3-bd33-4991-89f5-dfa96b69b26c"]>
to contain only:
  <["foo"]>
elements not found:
  <["foo"]>
and elements not expected:
  <["8665d91a-eab0-45a8-a378-e6603d66d084",
    "ff0c34c3-bd33-4991-89f5-dfa96b69b26c"]>
 */
    }

    @Test
    public void categories2() {
        final Reference<Category> longSleevesCategory = Reference.of(Category.referenceTypeId(), "eab84062-f4d7-4dd6-a576-ce5c10923e68");
        assertThat(list)
                .hasSize(5)
                .contains(list.get(0))
                .filteredOn(productProjection -> productProjection.getCategories().contains(longSleevesCategory))
                .extracting("id")
                .contains("foo")
                //negated
                .doesNotContain("bar");
    }

    @Test
    public void categories3() {
        final Reference<Category> longSleevesCategory = Reference.of(Category.referenceTypeId(), "eab84062-f4d7-4dd6-a576-ce5c10923e68");
        assertThat(list)
                .hasSize(5)
                .contains(list.get(0))
                .filteredOn(productProjection -> productProjection.getCategories().contains(longSleevesCategory))
                .extracting("id", Integer.class)//typing possible
                .contains(1)//works only in compilation so here it is an integer, runtime type is ignored
                .doesNotContain(3);
/*
java.lang.AssertionError:
Expecting:
<["8665d91a-eab0-45a8-a378-e6603d66d084",
        "ff0c34c3-bd33-4991-89f5-dfa96b69b26c"]>
to contain:
<[1]>
but could not find:
<[1]>
*/
    }

    @Test
    public void properties() {
        assertThat(list)
                .extracting("id", "version")//multiple extracting parameters force the contains parameters to be of tuple
                .contains(
                        tuple("baz"),
                        tuple("foo", 3)
                );
/*
java.lang.AssertionError:
Expecting:
 <[("8665d91a-eab0-45a8-a378-e6603d66d084", 4L),
    ("347caac1-daa2-4e31-bc95-e237ba1e2b91", 4L),
    ("f7edccd2-95d7-4fea-b511-ae5b3e309c7d", 4L),
    ("ff0c34c3-bd33-4991-89f5-dfa96b69b26c", 4L),
    ("461e2e52-50c9-4d77-a2f2-1cb77b601556", 4L)]>
to contain:
 <[("foo"), ("foo", 3)]>
but could not find:
 <[("foo"), ("foo", 3)]>
 */
    }

}










