package io.reflectoring.buckpal.account.application.service

import io.reflectoring.buckpal.account.application.port.`in`.SendMoneyCommand
import io.reflectoring.buckpal.account.application.port.`in`.SendMoneyUseCase
import io.reflectoring.buckpal.account.application.port.out.LoadAccountPort
import io.reflectoring.buckpal.account.application.port.out.UpdateAccountStatePort
import io.reflectoring.buckpal.common.UseCase
import org.springframework.transaction.annotation.Transactional

@UseCase
@Transactional
/**
 * 서비스는 인커밍 포트 인터페이스인 SendMoneyUseCase를 구현한다
 */
class SendMoneyService: SendMoneyUseCase {

    /**
     * 계좌를 불러오기 위해 아웃고잉 포트 인터페이스인 LoadAccountPort를 사용한다
     */
    private val loadAccountPort: LoadAccountPort = TODO()

    /**
     * 데이터베이스의 계좌 상태를 업데이트하기 위해
     * 아웃고잉 포트 인터페이스인 UpdateAccountStatePort를 사용한다
     */
    private val updateAccountStatePort: UpdateAccountStatePort = TODO()

    override fun sendMoney(command: SendMoneyCommand): Boolean {
        // TODO: 비즈니스 규칙 검증
        // TODO: 모델 상태 조작
        // TODO: 출력 값 반환
        return false
    }

}
