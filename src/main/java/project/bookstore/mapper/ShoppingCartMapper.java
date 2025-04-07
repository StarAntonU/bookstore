package project.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.bookstore.config.MapperConfig;
import project.bookstore.dto.shoppingcart.ShoppingCartDto;
import project.bookstore.model.ShoppingCart;

@Mapper(config = MapperConfig.class, componentModel = "spring", uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItemDtos", source = "cartItems")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
