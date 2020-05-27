package integration.com.sm.mastercard.send.config;

import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterByTypeTransformer;
import io.cucumber.datatable.TableCellByTypeTransformer;
import io.cucumber.datatable.TableEntryByTypeTransformer;
import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;

/**
 * Class used to convert DataTable to Java Object using Jackson (Json library).
 * <p>
 * <b>NOTE:</b> This class assumes that you will use field names match the data table column headers.
 * </p>
 */

public class Configure implements TypeRegistryConfigurer {

  @Override
  public void configureTypeRegistry(TypeRegistry registry) {

    JacksonTableTransformer jacksonTableTransformer = new JacksonTableTransformer();
    registry.setDefaultDataTableEntryTransformer(jacksonTableTransformer);
    registry.setDefaultDataTableCellTransformer(jacksonTableTransformer);
    registry.setDefaultParameterTransformer(jacksonTableTransformer);

  }

  @Override
  public Locale locale() {
    return Locale.ENGLISH;
  }

  private final class JacksonTableTransformer implements ParameterByTypeTransformer,TableEntryByTypeTransformer, TableCellByTypeTransformer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object transform(String s, Type type) {
      return objectMapper.convertValue(s, objectMapper.constructType(type));
    }

    @Override
    public <T> T transform(Map<String, String> entry, Class<T> type, TableCellByTypeTransformer cellTransformer) {
      return objectMapper.convertValue(entry, type);
    }

    @Override
    public <T> T transform(String value, Class<T> cellType) {
      return objectMapper.convertValue(value, cellType);
    }
  }
}