package eu.money.service.impl;

import eu.money.exception.AccountAlreadyLockedException;
import eu.money.model.Lock;
import eu.money.model.LockResourceType;
import eu.money.repository.LockRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class LockServiceImplTest {

    private LockRepository lockRepository = Mockito.mock(LockRepository.class);
    private Runnable runnable = Mockito.mock(Runnable.class);
    private LockServiceImpl lockService;

    @Before
    public void setUp() {
        lockService = new LockServiceImpl(lockRepository);
    }

    @Test
    public void shouldCreateLock() {
        // given
        Runnable runnable = Mockito.mock(Runnable.class);
        String accountId = "a12345";

        // when
        lockService.runInLock(accountId, runnable);

        // then
        Mockito.verify(lockRepository).save(new Lock(accountId, LockResourceType.ACCOUNT));
        Mockito.verify(lockRepository).delete(accountId, LockResourceType.ACCOUNT);
        Mockito.verify(runnable).run();
    }

    @Test
    public void shouldCreateTwoLocks() {
        // given
        String accountId = "a12345";
        String accountId2 = "a123456";

        // when
        lockService.runInLock(accountId, accountId2, runnable);

        // then
        Mockito.verify(lockRepository).save(new Lock(accountId, LockResourceType.ACCOUNT));
        Mockito.verify(lockRepository).save(new Lock(accountId2, LockResourceType.ACCOUNT));
        Mockito.verify(lockRepository).delete(accountId, LockResourceType.ACCOUNT);
        Mockito.verify(lockRepository).delete(accountId2, LockResourceType.ACCOUNT);
        Mockito.verify(runnable).run();
    }

    @Test(expected = AccountAlreadyLockedException.class)
    public void shouldFailIfLockExists() {
        // given
        Runnable runnable = Mockito.mock(Runnable.class);
        String accountId = "a12345";
        when(lockRepository.findByResourceIdAndType(accountId, LockResourceType.ACCOUNT)).thenReturn(Optional.of(new Lock(accountId, LockResourceType.ACCOUNT)));

        // when
        lockService.runInLock(accountId, runnable);
    }
}