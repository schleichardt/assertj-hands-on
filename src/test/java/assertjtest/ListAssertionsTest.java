package assertjtest;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.ProductProjection;
import org.assertj.core.api.Condition;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

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



    @Test
    public void m() {
        final ProductProjection productProjection = list.get(3);
        assertThat(productProjection.getId()).isEqualTo("461e2e52-50c9-4d77-a2f2-1cb77b601556");
        assertThat(productProjection.getProductType().getId()).isEqualTo("461e2e52-50c9-4d77-a2f2-1cb77b601556");
/*
org.junit.ComparisonFailure:
Expected :"461e2e52-50c9-4d77-a2f2-1cb77b601556"
Actual   :"ff0c34c3-bd33-4991-89f5-dfa96b69b26c"
 */

        //which one failed?
        //so you could split the method into two and use one assertion per test method
    }

    @Test
    public void m1() {
        final ProductProjection productProjection = list.get(3);
        assertThat(productProjection.getId()).as("id").isEqualTo("461e2e52-50c9-4d77-a2f2-1cb77b601556");
        assertThat(productProjection.getProductType().getId()).as("productType id").isEqualTo("461e2e52-50c9-4d77-a2f2-1cb77b601556");
/*
org.junit.ComparisonFailure: [id]
Expected :"461e2e52-50c9-4d77-a2f2-1cb77b601556"
Actual   :"ff0c34c3-bd33-4991-89f5-dfa96b69b26c"
 */

        //ah, it is the id
        //in fest assert overridingErrorMessage was used to achieve that, but the div was broken then
    }


    //use this so you don't forget to call assertAll()
    public static void softAssert(final Consumer<SoftAssertions> softlyConsumer) {
        final SoftAssertions softly = new SoftAssertions();
        softlyConsumer.accept(softly);
        softly.assertAll();
    }

    @Test
    public void softAssertions() {
        final Reference<Category> smartPhones = Reference.of(Category.referenceTypeId(), "xab84062-f4d7-4dd6-a576-ce5c10923e68");
        final ProductProjection productProjection = list.get(3);
        softAssert(softly -> {
            softly.assertThat(productProjection.getName().get(Locale.ENGLISH)).as("name").isEqualTo("Nexus 5");
            softly.assertThat(productProjection.getSlug().get(Locale.ENGLISH)).as("slug").isEqualTo("google-nexus-5");
            softly.assertThat(productProjection.getId()).as("id").isEqualTo("3b423f46-124e-4db5-b16a-1d68afabe640");
            softly.assertThat(productProjection.getCategories()).as("categories").contains(smartPhones);
        });
/*
org.assertj.core.api.SoftAssertionError:
The following 4 assertions failed:
1) [name] expected:<"[Nexus 5]"> but was:<"[MB PREMIUM TECH T]">
2) [slug] expected:<"[google-nexus-5]"> but was:<"[mb-premium-tech-t1417714797277]">
3) [id] expected:<"[3b423f46-124e-4db5-b16a-1d68afabe640]"> but was:<"[ff0c34c3-bd33-4991-89f5-dfa96b69b26c]">
4) [categories]
Expecting:
 <[Reference{typeId='category', id='eab84062-f4d7-4dd6-a576-ce5c10923e68', obj=null}]>
to contain:
 <[Reference{typeId='category', id='xab84062-f4d7-4dd6-a576-ce5c10923e68', obj=null}]>
but could not find:
 <[Reference{typeId='category', id='xab84062-f4d7-4dd6-a576-ce5c10923e68', obj=null}]>
 */
    }


    public static Condition<ProductProjection> priceWithId(final String id) {
        return new Condition<ProductProjection>(String.format("price with id='%s'", id)) {
            @Override
            public boolean matches(final ProductProjection value) {
                //TODO also for variants
                return value.getMasterVariant().getPrices().stream().anyMatch(p -> p.getId().equals(id));
            }
        };
    }

    @Test
    public void conditions() {
        final ProductProjection productProjection = list.get(3);
        assertThat(productProjection).has(priceWithId("1513154a-5cc9-4b2f-88a3-4e096ae92f0e"));
/*
java.lang.AssertionError:
Expecting:
 <ProductProjectionImpl[productType=Reference{typeId='product-type', id='c6af7304-4e35-48d6-8687-66ca2dab5dbb', obj=null},taxCategory=Reference{typeId='tax-category', id='0b423f46-124e-4db5-b16a-1d68afabe640', obj=null},isPublished=true,hasStagedChanges=true,name=LocalizedString(en -> MB PREMIUM TECH T),categories=[Reference{typeId='category', id='eab84062-f4d7-4dd6-a576-ce5c10923e68', obj=null}],description=LocalizedString(en -> Sample description),slug=LocalizedString(en -> mb-premium-tech-t1417714797277),metaTitle=<null>,metaDescription=<null>,metaKeywords=<null>,masterVariant=ProductVariantImpl[productId=ff0c34c3-bd33-4991-89f5-dfa96b69b26c,id=1,sku=sku_MB_PREMIUM_TECH_T_variant1_1417714797277,prices=[Price[value=MoneyImpl[money=EUR 100.00000],country=<null>,customerGroup=<null>,channel=<null>,discounted=<null>,validFrom=<null>,validUntil=<null>,id=02e4c5ea-89aa-45d7-b571-3761ddc1a302]],images=[ImageImpl[url=https://www.commercetools.com/cli/data/253245821_1.jpg,dimensions=ImageDimensions[width=1400,height=1400],label=<null>]],availability=<null>,attributes=[]],variants=[],searchKeywords=SearchKeywords[content={}],id=ff0c34c3-bd33-4991-89f5-dfa96b69b26c,version=4,createdAt=2014-12-04T17:39:57.296Z,lastModifiedAt=2015-05-23T09:28:02.152Z]>
to have:
 <price with id='1513154a-5cc9-4b2f-88a3-4e096ae92f0e'>
 */
    }

    @Test
    public void conditionsList() {
        assertThat(list).have(priceWithId("403f7308-f786-4fd3-9d98-2ccf4cb6f590"));
/*
java.lang.AssertionError:
Expecting elements:
<[ProductProjectionImpl[productType=Reference{typeId='product-type', id='c6af7304-4e35-48d6-8687-66ca2dab5dbb', obj=null},taxCategory=Reference{typeId='tax-category', id='0b423f46-124e-4db5-b16a-1d68afabe640', obj=null},isPublished=true,hasStagedChanges=true,name=LocalizedString(en -> GIRLS HARTBREAK CREW),categories=[Reference{typeId='category', id='eab84062-f4d7-4dd6-a576-ce5c10923e68', obj=null}],description=LocalizedString(en -> Sample description),slug=LocalizedString(en -> girls-hartbreak-crew1417714797195),metaTitle=<null>,metaDescription=<null>,metaKeywords=<null>,masterVariant=ProductVariantImpl[productId=8665d91a-eab0-45a8-a378-e6603d66d084,id=1,sku=sku_GIRLS_HARTBREAK_CREW_variant1_1417714797195,prices=[Price[value=MoneyImpl[money=EUR 34.00000],country=<null>,customerGroup=<null>,channel=<null>,discounted=<null>,validFrom=<null>,validUntil=<null>,id=1513154a-5cc9-4b2f-88a3-4e096ae92f0e]],images=[ImageImpl[url=https://www.commercetools.com/cli/data/253234387_1.jpg,dimensions=ImageDimensions[width=1400,height=1400],label=<null>]],availability=<null>,attributes=[]],variants=[],searchKeywords=SearchKeywords[content={}],id=8665d91a-eab0-45a8-a378-e6603d66d084,version=4,createdAt=2014-12-04T17:39:57.214Z,lastModifiedAt=2015-05-23T09:28:02.086Z],
    ProductProjectionImpl[productType=Reference{typeId='product-type', id='c6af7304-4e35-48d6-8687-66ca2dab5dbb', obj=null},taxCategory=Reference{typeId='tax-category', id='0b423f46-124e-4db5-b16a-1d68afabe640', obj=null},isPublished=true,hasStagedChanges=true,name=LocalizedString(en -> MB SOFTSHELL LINER),categories=[Reference{typeId='category', id='3f3d7425-b1df-473b-9425-4cddba412f3e', obj=null}],description=LocalizedString(en -> Sample description),slug=LocalizedString(en -> mb-softshell-liner1417714797242),metaTitle=<null>,metaDescription=<null>,metaKeywords=<null>,masterVariant=ProductVariantImpl[productId=f7edccd2-95d7-4fea-b511-ae5b3e309c7d,id=1,sku=sku_MB_SOFTSHELL_LINER_variant1_1417714797242,prices=[Price[value=MoneyImpl[money=EUR 100.00000],country=<null>,customerGroup=<null>,channel=<null>,discounted=<null>,validFrom=<null>,validUntil=<null>,id=b6ff373f-506e-46b1-af00-8ef0c7fd2e64]],images=[ImageImpl[url=https://www.commercetools.com/cli/data/254391631_1.jpg,dimensions=ImageDimensions[width=1400,height=1400],label=<null>]],availability=<null>,attributes=[]],variants=[],searchKeywords=SearchKeywords[content={}],id=f7edccd2-95d7-4fea-b511-ae5b3e309c7d,version=4,createdAt=2014-12-04T17:39:57.271Z,lastModifiedAt=2015-05-23T09:28:02.130Z],
    ProductProjectionImpl[productType=Reference{typeId='product-type', id='c6af7304-4e35-48d6-8687-66ca2dab5dbb', obj=null},taxCategory=Reference{typeId='tax-category', id='0b423f46-124e-4db5-b16a-1d68afabe640', obj=null},isPublished=true,hasStagedChanges=true,name=LocalizedString(en -> MB PREMIUM TECH T),categories=[Reference{typeId='category', id='eab84062-f4d7-4dd6-a576-ce5c10923e68', obj=null}],description=LocalizedString(en -> Sample description),slug=LocalizedString(en -> mb-premium-tech-t1417714797277),metaTitle=<null>,metaDescription=<null>,metaKeywords=<null>,masterVariant=ProductVariantImpl[productId=ff0c34c3-bd33-4991-89f5-dfa96b69b26c,id=1,sku=sku_MB_PREMIUM_TECH_T_variant1_1417714797277,prices=[Price[value=MoneyImpl[money=EUR 100.00000],country=<null>,customerGroup=<null>,channel=<null>,discounted=<null>,validFrom=<null>,validUntil=<null>,id=02e4c5ea-89aa-45d7-b571-3761ddc1a302]],images=[ImageImpl[url=https://www.commercetools.com/cli/data/253245821_1.jpg,dimensions=ImageDimensions[width=1400,height=1400],label=<null>]],availability=<null>,attributes=[]],variants=[],searchKeywords=SearchKeywords[content={}],id=ff0c34c3-bd33-4991-89f5-dfa96b69b26c,version=4,createdAt=2014-12-04T17:39:57.296Z,lastModifiedAt=2015-05-23T09:28:02.152Z],
    ProductProjectionImpl[productType=Reference{typeId='product-type', id='c6af7304-4e35-48d6-8687-66ca2dab5dbb', obj=null},taxCategory=Reference{typeId='tax-category', id='0b423f46-124e-4db5-b16a-1d68afabe640', obj=null},isPublished=true,hasStagedChanges=true,name=LocalizedString(en -> SAPPHIRE),categories=[Reference{typeId='category', id='3f3d7425-b1df-473b-9425-4cddba412f3e', obj=null}],description=LocalizedString(en -> Sample description),slug=LocalizedString(en -> sapphire1417714797145),metaTitle=<null>,metaDescription=<null>,metaKeywords=<null>,masterVariant=ProductVariantImpl[productId=461e2e52-50c9-4d77-a2f2-1cb77b601556,id=1,sku=sku_SAPPHIRE_variant1_1417714797145,prices=[Price[value=MoneyImpl[money=EUR 28.00000],country=<null>,customerGroup=<null>,channel=<null>,discounted=<null>,validFrom=<null>,validUntil=<null>,id=b6fbf092-55eb-40ac-9ca9-336ca401b252]],images=[ImageImpl[url=https://www.commercetools.com/cli/data/252542005_1.jpg,dimensions=ImageDimensions[width=1400,height=1400],label=<null>]],availability=<null>,attributes=[]],variants=[],searchKeywords=SearchKeywords[content={}],id=461e2e52-50c9-4d77-a2f2-1cb77b601556,version=4,createdAt=2014-12-04T17:39:57.189Z,lastModifiedAt=2015-05-23T09:28:02.058Z]]>
 of
<[ProductProjectionImpl[productType=Reference{typeId='product-type', id='c6af7304-4e35-48d6-8687-66ca2dab5dbb', obj=null},taxCategory=Reference{typeId='tax-category', id='0b423f46-124e-4db5-b16a-1d68afabe640', obj=null},isPublished=true,hasStagedChanges=true,name=LocalizedString(en -> GIRLS HARTBREAK CREW),categories=[Reference{typeId='category', id='eab84062-f4d7-4dd6-a576-ce5c10923e68', obj=null}],description=LocalizedString(en -> Sample description),slug=LocalizedString(en -> girls-hartbreak-crew1417714797195),metaTitle=<null>,metaDescription=<null>,metaKeywords=<null>,masterVariant=ProductVariantImpl[productId=8665d91a-eab0-45a8-a378-e6603d66d084,id=1,sku=sku_GIRLS_HARTBREAK_CREW_variant1_1417714797195,prices=[Price[value=MoneyImpl[money=EUR 34.00000],country=<null>,customerGroup=<null>,channel=<null>,discounted=<null>,validFrom=<null>,validUntil=<null>,id=1513154a-5cc9-4b2f-88a3-4e096ae92f0e]],images=[ImageImpl[url=https://www.commercetools.com/cli/data/253234387_1.jpg,dimensions=ImageDimensions[width=1400,height=1400],label=<null>]],availability=<null>,attributes=[]],variants=[],searchKeywords=SearchKeywords[content={}],id=8665d91a-eab0-45a8-a378-e6603d66d084,version=4,createdAt=2014-12-04T17:39:57.214Z,lastModifiedAt=2015-05-23T09:28:02.086Z],
    ProductProjectionImpl[productType=Reference{typeId='product-type', id='c6af7304-4e35-48d6-8687-66ca2dab5dbb', obj=null},taxCategory=Reference{typeId='tax-category', id='0b423f46-124e-4db5-b16a-1d68afabe640', obj=null},isPublished=true,hasStagedChanges=true,name=LocalizedString(en -> WB ATHLETIC TANK),categories=[Reference{typeId='category', id='0ff5fd1d-f7c9-4ba3-a3dc-d74c417526c6', obj=null}],description=LocalizedString(en -> Sample description),slug=LocalizedString(en -> wb-athletic-tank1417714797219),metaTitle=<null>,metaDescription=<null>,metaKeywords=<null>,masterVariant=ProductVariantImpl[productId=347caac1-daa2-4e31-bc95-e237ba1e2b91,id=1,sku=sku_WB_ATHLETIC_TANK_variant1_1417714797219,prices=[Price[value=MoneyImpl[money=EUR 84.00000],country=<null>,customerGroup=<null>,channel=<null>,discounted=<null>,validFrom=<null>,validUntil=<null>,id=403f7308-f786-4fd3-9d98-2ccf4cb6f590]],images=[ImageImpl[url=https://www.commercetools.com/cli/data/253265444_1.jpg,dimensions=ImageDimensions[width=1400,height=1400],label=<null>]],availability=<null>,attributes=[]],variants=[],searchKeywords=SearchKeywords[content={}],id=347caac1-daa2-4e31-bc95-e237ba1e2b91,version=4,createdAt=2014-12-04T17:39:57.234Z,lastModifiedAt=2015-05-23T09:28:02.108Z],
    ProductProjectionImpl[productType=Reference{typeId='product-type', id='c6af7304-4e35-48d6-8687-66ca2dab5dbb', obj=null},taxCategory=Reference{typeId='tax-category', id='0b423f46-124e-4db5-b16a-1d68afabe640', obj=null},isPublished=true,hasStagedChanges=true,name=LocalizedString(en -> MB SOFTSHELL LINER),categories=[Reference{typeId='category', id='3f3d7425-b1df-473b-9425-4cddba412f3e', obj=null}],description=LocalizedString(en -> Sample description),slug=LocalizedString(en -> mb-softshell-liner1417714797242),metaTitle=<null>,metaDescription=<null>,metaKeywords=<null>,masterVariant=ProductVariantImpl[productId=f7edccd2-95d7-4fea-b511-ae5b3e309c7d,id=1,sku=sku_MB_SOFTSHELL_LINER_variant1_1417714797242,prices=[Price[value=MoneyImpl[money=EUR 100.00000],country=<null>,customerGroup=<null>,channel=<null>,discounted=<null>,validFrom=<null>,validUntil=<null>,id=b6ff373f-506e-46b1-af00-8ef0c7fd2e64]],images=[ImageImpl[url=https://www.commercetools.com/cli/data/254391631_1.jpg,dimensions=ImageDimensions[width=1400,height=1400],label=<null>]],availability=<null>,attributes=[]],variants=[],searchKeywords=SearchKeywords[content={}],id=f7edccd2-95d7-4fea-b511-ae5b3e309c7d,version=4,createdAt=2014-12-04T17:39:57.271Z,lastModifiedAt=2015-05-23T09:28:02.130Z],
    ProductProjectionImpl[productType=Reference{typeId='product-type', id='c6af7304-4e35-48d6-8687-66ca2dab5dbb', obj=null},taxCategory=Reference{typeId='tax-category', id='0b423f46-124e-4db5-b16a-1d68afabe640', obj=null},isPublished=true,hasStagedChanges=true,name=LocalizedString(en -> MB PREMIUM TECH T),categories=[Reference{typeId='category', id='eab84062-f4d7-4dd6-a576-ce5c10923e68', obj=null}],description=LocalizedString(en -> Sample description),slug=LocalizedString(en -> mb-premium-tech-t1417714797277),metaTitle=<null>,metaDescription=<null>,metaKeywords=<null>,masterVariant=ProductVariantImpl[productId=ff0c34c3-bd33-4991-89f5-dfa96b69b26c,id=1,sku=sku_MB_PREMIUM_TECH_T_variant1_1417714797277,prices=[Price[value=MoneyImpl[money=EUR 100.00000],country=<null>,customerGroup=<null>,channel=<null>,discounted=<null>,validFrom=<null>,validUntil=<null>,id=02e4c5ea-89aa-45d7-b571-3761ddc1a302]],images=[ImageImpl[url=https://www.commercetools.com/cli/data/253245821_1.jpg,dimensions=ImageDimensions[width=1400,height=1400],label=<null>]],availability=<null>,attributes=[]],variants=[],searchKeywords=SearchKeywords[content={}],id=ff0c34c3-bd33-4991-89f5-dfa96b69b26c,version=4,createdAt=2014-12-04T17:39:57.296Z,lastModifiedAt=2015-05-23T09:28:02.152Z],
    ProductProjectionImpl[productType=Reference{typeId='product-type', id='c6af7304-4e35-48d6-8687-66ca2dab5dbb', obj=null},taxCategory=Reference{typeId='tax-category', id='0b423f46-124e-4db5-b16a-1d68afabe640', obj=null},isPublished=true,hasStagedChanges=true,name=LocalizedString(en -> SAPPHIRE),categories=[Reference{typeId='category', id='3f3d7425-b1df-473b-9425-4cddba412f3e', obj=null}],description=LocalizedString(en -> Sample description),slug=LocalizedString(en -> sapphire1417714797145),metaTitle=<null>,metaDescription=<null>,metaKeywords=<null>,masterVariant=ProductVariantImpl[productId=461e2e52-50c9-4d77-a2f2-1cb77b601556,id=1,sku=sku_SAPPHIRE_variant1_1417714797145,prices=[Price[value=MoneyImpl[money=EUR 28.00000],country=<null>,customerGroup=<null>,channel=<null>,discounted=<null>,validFrom=<null>,validUntil=<null>,id=b6fbf092-55eb-40ac-9ca9-336ca401b252]],images=[ImageImpl[url=https://www.commercetools.com/cli/data/252542005_1.jpg,dimensions=ImageDimensions[width=1400,height=1400],label=<null>]],availability=<null>,attributes=[]],variants=[],searchKeywords=SearchKeywords[content={}],id=461e2e52-50c9-4d77-a2f2-1cb77b601556,version=4,createdAt=2014-12-04T17:39:57.189Z,lastModifiedAt=2015-05-23T09:28:02.058Z]]>
 to have <price with id='403f7308-f786-4fd3-9d98-2ccf4cb6f590'>
 */
    }
}










