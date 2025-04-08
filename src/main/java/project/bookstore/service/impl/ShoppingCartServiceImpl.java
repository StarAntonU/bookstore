package project.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.dto.cartitem.CreateCartItemRequestDto;
import project.bookstore.dto.cartitem.UpdateCartItemDto;
import project.bookstore.dto.shoppingcart.ShoppingCartDto;
import project.bookstore.exception.unchecked.EntityNotFoundException;
import project.bookstore.mapper.CartItemMapper;
import project.bookstore.mapper.ShoppingCartMapper;
import project.bookstore.model.Book;
import project.bookstore.model.CartItem;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.User;
import project.bookstore.repository.book.BookRepository;
import project.bookstore.repository.cartitem.CartItemRepository;
import project.bookstore.repository.shoppingcart.ShoppingCartRepository;
import project.bookstore.service.ShoppingCartService;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartDto addItem(
            CreateCartItemRequestDto requestDto, Authentication authentication) {
        Long userId = findUser(authentication);
        Book book = bookRepository.findById(requestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Can`t find book by id "
                        + requestDto.bookId()));
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(requestDto.bookId()))
                .findFirst()
                .ifPresentOrElse(item -> item.setQuantity(item.getQuantity()
                                + requestDto.quantity()),
                        () -> addCartItemToCart(requestDto, book, cart));
        return shoppingCartMapper.toDto(shoppingCartRepository.save(cart));
    }

    @Override
    public ShoppingCartDto updateCartItem(Long id,
                                          UpdateCartItemDto requestDto,
                                          Authentication authentication) {
        Long userId = findUser(authentication);
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, cart.getId())
                .map(item -> {
                    item.setQuantity(requestDto.getQuantity());
                    return item;
                }).orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find cart item by id " + id));
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        Long userId = findUser(authentication);
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public void delete(Long id) {
        if (!cartItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Can`t find item by id " + id);
        }
        cartItemRepository.deleteById(id);
    }

    @Override
    public void createNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private Long findUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }

    private void addCartItemToCart(
            CreateCartItemRequestDto itemDto, Book book, ShoppingCart cart) {
        CartItem cartItem = cartItemMapper.toModel(itemDto);
        cartItem.setBook(book);
        cart.addItemToCart(cartItem);
    }
}
