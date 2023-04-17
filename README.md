# AOP, Filter 순서
Spring에서는 다음과 같은 순서로 AOP와 Filter가 적용됩니다.

- AOP의 Before Advice
- Filter의 preHandler
- AOP의 Around Advice
- Controller의 핸들러 메서드
- AOP의 AfterReturning Advice
- Filter의 postHandler
- AOP의 AfterThrowing Advice
- AOP의 After Advice

# 토큰

만약, 해커가 JWT AccessToken을 탈취한다면?

해커는 탈취한 AccessToken을 사용하여 접근이 모두 가능해질 것입니다.

이를 해결하기 위해, AccessToken의 유효 기간을 짧게 하면 해결할 수 있습니다.

만약 해커가 탈취를 하더라도, 그 AccessToken을 짧은 시간동안 밖에 못 쓰기 때문에 대응이 되는 것입니다.


하지만, AccessToken의 유효 기간을 짧게 설정하게 되면 유저 입장에서 매우 귀찮고 번거로울 것입니다.

로그인 한지 얼마 되지도 않아 다른 페이지를 이동할 때

다시 로그인을 해서 AccessToken을 발급 받아야하기 때문입니다.

따라서, 해커 탈취 문제-사용자의 이용성에 trade-off가 발생하게 되는 것입니다.


이러한 trade-off를 해결해 주는 것이 바로 RefreshToken입니다.

RefreshToken은 인증이 아닌, AccessToken을 재발급 해주는 역할의 Token입니다.

따라서, RefreshToken만 가지고는 인증을 성공할 수 없습니다.

AccessToken과 RefreshToken 모두 JWT이지만, 서로 역할이 다릅니다.


1. AccessToken
   처음 로그인 요청 시 서버에서 실제 유저의 정보가 담긴 AccessToken을 발행합니다.
   클라이언트는 이 AccessToken을 저장한 후, 요청마다 AccessToken을 보내서
   해당 AccessToken을 서버에서 검증 후 유효하면 요청에 맞는 응답을 진행합니다.

2. RefreshToken
   처음 로그인 요청 시 서버에서 AccessToken 재발급 용도인 RefreshToken을 발행합니다.
   이때, 클라이언트는 RefreshToken을 저장하지 않고 RefreshToken은 보통 서버 DB에 저장됩니다.
   RefreshToken이 유효하면, AccessToken의 재발급을 진행합니다.
   그렇다면, 구체적으로 어떻게 RefreshToken이

해커 탈취 문제-사용자의 이용성 trade-off를 해결할 수 있는지 살펴봅시다.

먼저, 해커 탈취 문제 대응을 위해 AccessToken의 유효 기간을 1일에서 1시간으로 변경했다고 해봅시다.

만약, RefreshToken이 없다면 위에서 언급했던 것처럼 사용자는 1시간마다 로그인을 해줘야할 것입니다.



여기서, RefreshToken은 유효 기간이 7일이라고 해봅시다.

AccessToken이 1시간이 지나 만료 후 클라이언트가 요청을 보낼 때,

RefreshToken 로직이 추가되면, 서버에서는 인증 실패가 아닌 RefreshToken 검증 단계에 진입합니다.

RefreshToken이 유효하다면, 그 즉시 클라이언트에게 새로운 AccessToken을 발행해주고,

클라이언트는 그 AccessToken을 받아 재요청을 하게 됩니다.

따라서, 사용자의 눈에는 별도의 재로그인 과정없이 AccessToken이 만료되지 않은 것처럼 동작하게 됩니다.



RefreshToken의 유효 기간이 7일이기 때문에,

결국 사용자는 AccessToken의 유효 기간이 7일인 것처럼 사용이 가능한 것입니다.

물론 RefreshToken도 해커에게 탈취되면 AccessToken을 해커가 재발급 받을 수 있기 때문에 위험하지만,

RefreshToken은 클라이언트에 저장되는 것이 아닌 서버 DB에 저장되기 때문에, 해커 탈취 위험이 적습니다.

이렇게 해커 탈취 문제-사용자의 이용성 trade-off를 해결하는 것입니다.

# OncePerRequestFilter와 GenericFilterBean의 차이

OncePerRequestFilter의 설명은 다음과 같습니다.

모든 서블릿 컨테이너에서 요청 디스패치당 단일 실행을 보장하는 것을 목표로 하는 필터 기본 클래스입니다.

OncePerRequestFilter와 GenericFilterBean 모두 Spring Security에서 사용하는 필터(Filter) 클래스입니다. 이 두 클래스는 모두 javax.servlet.Filter 인터페이스를 구현하며, doFilter() 메서드를 재정의하여 필터링 로직을 구현합니다.

그러나 두 클래스의 차이점은 다음과 같습니다.

OncePerRequestFilter: Spring Security에서 제공하는 필터 클래스로, ServletRequest에 대해 단 한 번만 필터링을 수행합니다. 즉, 같은 요청에 대해 중복으로 필터링을 수행하지 않습니다. 이 클래스는 추상 클래스로, 구현체에서는 doFilterInternal() 메서드를 구현하여 필터링 로직을 구현합니다.

GenericFilterBean: Spring에서 제공하는 일반적인 필터 클래스로, ServletRequest에 대해 여러 번 필터링을 수행할 수 있습니다. 이 클래스는 추상 클래스가 아니며, 구현체에서는 doFilter() 메서드를 구현하여 필터링 로직을 구현합니다.

즉, OncePerRequestFilter는 ServletRequest에 대해 단 한 번만 필터링을 수행하므로, 중복 필터링을 방지할 수 있습니다. 반면에 GenericFilterBean은 ServletRequest에 대해 여러 번 필터링을 수행할 수 있습니다.

따라서, Spring Security에서 같은 요청에 대해 여러 번 필터링이 필요한 경우에는 GenericFilterBean을 사용하고, 중복 필터링을 방지해야 하는 경우에는 OncePerRequestFilter를 사용하는 것이 좋습니다.

# AccessToken 만료 전 인증 과정

1. 클라이언트가 이메일/비밀번호을 담아 로그인 요청을 서버에 보냅니다.

2. 서버는 요청 받은 이메일/비밀번호로 DB에서 유저를 찾고,
   유저가 존재한다면 AccessToken과 RefreshToken을 생성하여 Response에 담아 반환합니다.
   (이때, 생성한 RefreshToken은 DB에 저장해둡니다.)

3. 이후 클라이언트는 매 요청 시마다 AccessToken을 담아 API를 요청합니다.

4. 서버에서는 요청받은 AccessToken을 검증하여 인증 성공/인증 실패 처리를 합니다.
# AccessToken 만료 후 인증 과정

1. 클라이언트에서 자체적으로 AccessToken 만료를 판단한 후, 서버에 RefreshToken만을 담아 요청합니다.

2. 서버에서 요청 받은 RefreshToken이 DB에 저장된 Refresh과 일치하는지 판단 후,
   일치한다면 AccessToken과 RefreshToken을 재발급하여 Response에 담아 보냅니다.
   이때, 재발급한 RefreshToken으로 DB의 RefreshToken을 업데이트합니다. (RTR 방식)

3. 클라이언트는 서버로부터 재발급 받은 AccessToken을 요청에 담아 API 요청을 보냅니다.

4. 서버에서 요청 받은 AccessToken을 검증하여 인증 성공/인증 실패 처리를 합니다.

RTR이란 Refresh Token Rotation 방식의 약자로, Refresh Token을 한번만 사용할 수 있게 만드는 방법입니다.
RefreshToken을 사용하여 만료된 AccessToken을 재발급 받을 때, Refresh Token도 재발급하는 방법입니다.
이러한 방식이 나온 이유는, RefreshToken이 탈취된다면 AccessToken을 계속 생성할 수 있기 때문입니다.
RefreshToken은 만료 기간이 길기 때문에 이러한 상황이 된다면 상당히 위험해집니다.

# 자체 JSON 로그인 커스텀 하는 이유 
Spring Security에서는 일반 Form Login을 기본적으로 제공하지만,

JSON 형식의 RequestBody로 하는 로그인 방식은 기본적으로 제공하지 않습니다.

따라서, JSON 로그인 방식을 사용하기 위해 커스텀 필터를 구현해야합니다.

이때, Form Login 시 기본적으로 사용되는 UsernamePasswordAuthenticationFilter의 코드를 참고하여 구현합니다.

참고 : https://ttl-blog.tistory.com/268