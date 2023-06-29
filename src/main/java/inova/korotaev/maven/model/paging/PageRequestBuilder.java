package inova.korotaev.maven.model.paging;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@NoArgsConstructor
public class PageRequestBuilder {
    private Integer offset;
    private Integer limit;
    private List<Sort.Order> sortOrders;
}
