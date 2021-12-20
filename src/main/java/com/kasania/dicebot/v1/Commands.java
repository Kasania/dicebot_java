/*
 * Author : Kasania
 * Filename : Commands
 * Desc :
 */
package com.kasania.dicebot.v1;

import com.kasania.dicebot.common.SimpleEmbedMessage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public enum Commands {

    radd("CoC 7th 시트링크 별명 설정",
            """
                    !radd [시트링크별명] [시트링크]
                    !radd 테스트용별명 https://docs.google.com/spreadsheets/d/1CzAo97L-ioGFHo_d8MC64nxAKiLchd-MkixYL4mxjwE

                    * 이 기능은 `TRPG 마스터` 역할이 있어야만 사용 가능합니다. *
                    !ruse 명령어에서 사용가능한 시트링크 별명을 설정 할 수 있습니다.
                    !radd 명령어로 등록한 시트링크 별명은 !ruse 명령어에서 시트링크와 동일하게 동작합니다."""),


    rclear("등록된 CoC 7th 시트 전체 해제",
            """
                    !rclear\s
                    !rclear 테스트용별명

                    * 이 기능은 `TRPG 마스터` 역할이 있어야만 사용 가능합니다. *
                    !ruse 명령어로 등록된 시트를 전부 등록 해제하는 기능입니다.
                    * 현재 서버에서 !ruse 명령어로 등록된 모든 시트를 해제하므로 주의하시기 바랍니다. *"""),

    rremove("CoC 7th 시트링크 별명 제거",
            """
                    !rremove [시트링크별명]
                    !rremove 테스트용별명

                    * 이 기능은 `TRPG 마스터` 역할이 있어야만 사용 가능합니다. *
                    !radd 명령어로 등록한 시트링크 별명을 제거하는 기능입니다."""),


    rccc("CoC 7th 탐사자 특성치 굴리기",
            """
                    !rccc\s

                    CoC 7th 탐사자의 생성에 필요한 특성치 주사위를 굴리는 기능입니다."""),

    rr("CoC 7th 다이스 굴리기",
            """
                    !rr [기능/특성치 이름] (주사위 표현식)
                    !rr 근력
                    !rr 근접전(격투) +1d6
                    !rr 기계수리 b1

                    !ruse로 등록한 CoC 7th 시트의 주사위를 굴릴 수 있습니다.
                    공개된 CoC 7th 판정 기준을 따라 대성공/어려운성공/성공/실패/대실패를 판별합니다.
                    추가적인 주사위 표현식을 사용하여 판정에 보정을 적용 할 수 있습니다.
                    b, p 연산을 사용하여 보너스, 페널티 주사위를 적용 할 수 있습니다."""),

    rreset("등록된 CoC 7th 시트 해제",
            """
                    !rreset\s

                    !ruse 명령으로 등록한 시트를 해제하는 기능입니다."""),

    rstat("등록된 CoC 7th 시트 확인", """
            !rstat\s

            !ruse 명령으로 현재 등록된 시트를 확인하는 기능입니다."""),

    ruse("CoC 7th 다이스 굴리기 시트 등록",
            """
                    !ruse [시트링크] [시트이름]
                    !ruse [시트링크별명] [시트이름]
                    !ruse https://docs.google.com/spreadsheets/d/1CzAo97L-ioGFHo_d8MC64nxAKiLchd-MkixYL4mxjwE 조 종사(Niq)
                    !ruse 시트링크별명 조 종사(Niq)

                    !rr 명령에 사용할 CoC 7th 시트를 등록하는 명령어입니다.
                    구글 스프레드 시트의 링크 또는 !radd 명령을 통해 별명이 지정된 시트의 별명을 사용 할 수 있습니다.
                    등록에 사용할 수 있는 서식은 https://stone-whale.postype.com/post/4912082의 기본 및 now 시트입니다.
                    등록한 시트는 서버 및 개인별로 별도로 저장됩니다."""),


    r("기본 주사위 굴림 명령어",
            """
                    !r [주사위 표현식]
                    !r 1d100+20
                    !r (1d20+4d20h2)>=50

                    [dice expr]에는 사칙연산 및 다이스 연산으로 표현된 주사위 표현식이 사용됩니다.
                    사용가능한 표현식은 아래와 같습니다.

                     - 사칙연산자(+,-,*,/), 비교연산자(<,>,<=,>=) 및 괄호
                     - NdR : N개의 R면체 주사위 굴리기
                     - Ndf : N개의 fudge(fate) 주사위 굴리기
                     - NdRhX : N개의 R면체 주사위를 굴리고, 값이 높은 순서대로 X개의 주사위만 사용
                     - NdRlX : N개의 R면체 주사위를 굴리고, 값이 낮은 순서대로 X개의 주사위만 사용

                    각 연산사이에 사칙, 비교연산자를 사용하여 연결할 수 있으며, 비교 연산 후에는 성공/실패에 따라 값이 1/0으로 처리됩니다.
                    NdRhX 혹은 NdRlX 연산시에 d와 h 사이에 추가적인 연산을 하는 경우, 올바르지 않은 값이 출력될 수 있습니다."""),

    ra("주사위 표현식 별명 설정",
            """
                    !ra [별명] [주사위 표현식]
                    !ra crit 3d12+4
                    !ra atk 1d20+6<=

                    자주 사용하는 주사위 표현식에 별명을 지정할 수 있습니다.
                    별명이 지정된 표현식은 !rt 명령어를 통해 편리하게 굴릴 수 있습니다.
                    미완성 표현식에도 별명을 지정할 수 있습니다. 단, !rt 명령어에서 미완성된 표현식을 완성해야 합니다.
                    별명에 띄어쓰기를 사용하는 경우 큰따옴표("")를 통해 묶어주어야 합니다.
                    등록한 별명은 서버 및 개인별로 별도로 저장됩니다."""),

    rd("별명이 지정된 주사위 표현식 제거", """
            !rd [별명]
            !rd crit

            !rd 명령어로 별명이 지정된 주사위 표현식을 제거합니다.
            개인/서버별로 분리되어 동작합니다."""),

    rl("별명이 지정된 주사위 표현식 목록 출력",
            """
                    !rl\s

                    !ra 명령어로 별명이 지정된 주사위 표현식의 목록을 출력합니다."""),

    rt("별명이 지정된 주사위 표현식 굴리기",
            """
                    !rt [별명] (추가 주사위 표현식)
                    !rt crit
                    !rt atk 15

                    !ra 명령어로 별명이 지정된 주사위 표현식을 굴립니다.
                    별명으로 지정된 표현식에 이어서 추가적인 표현식을 입력할 수 있습니다.
                    별명에 띄어쓰기를 사용한 경우 큰따옴표("")를 통해 묶어주어야 합니다."""),

    ;

    static {

        BasicDice basicDice = new BasicDice();

        r.eventHandler = basicDice::command_r;
        ra.eventHandler = basicDice::command_ra;
        rd.eventHandler = basicDice::command_rd;
        rl.eventHandler = basicDice::command_rl;
        rt.eventHandler = basicDice::command_rt;

    }

    private final Logger logger = LoggerFactory.getLogger(Commands.class);

    public final String simpleHelpMessage;
    public final String fullHelpMessage;

    private Consumer<MessageReceivedEvent> eventHandler;

    //TODO : fullHelpMessage improve
    Commands(String helpMessage, String fullHelpMessage){
        this.simpleHelpMessage = helpMessage;
        this.fullHelpMessage = fullHelpMessage.stripIndent();
    }

    public void execute(@NotNull MessageReceivedEvent event){
        String channel;
        if(event.isFromThread()){
            channel = event.getThreadChannel().getParentMessageChannel().getName()
                    + " - " + event.getThreadChannel().getName();
        }else{
            channel = event.getTextChannel().getName();
        }

        logger.info("[{}][{}] {}: {}", event.getGuild().getName(),
                channel,
                event.getMember().getEffectiveName(),
                event.getMessage().getContentDisplay());

        try{
            eventHandler.accept(event);
        }catch (Exception e){
            event.getMessage().replyEmbeds(SimpleEmbedMessage.titleDescEmbed(":x: 명령을 실행하는데 실패했습니다.",
                    "!다이스 "+this.name()+" 명령어를 확인해주세요.")).queue();
        }

    }




}
