/*
 * Author : Kasania
 * Filename : Commands
 * Desc :
 */
package com.kasania.dicebot.v1;

import com.kasania.dicebot.common.SimpleEmbedMessage;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public enum Commands {

    rdnd("DnD 5th 능력치 굴리기",
            """
                    !rdnd\s

                    DnD 5th 캐릭터의 생성에 필요한 능력치 주사위를 굴리는 기능입니다."""),

    rccc("CoC 7th 탐사자 특성치 굴리기",
            """
                    !rccc\s

                    CoC 7th 탐사자의 생성에 필요한 특성치 주사위를 굴리는 기능입니다."""),

    ruse("CoC 7th 다이스 굴리기 시트 등록",
            """
                    !ruse [시트링크]
                    !ruse https://docs.google.com/spreadsheets/d/1CzAo97L-ioGFHo_d8MC64nxAKiLchd-MkixYL4mxjwE/edit#gid=2107011865
                    !ruse https://docs.google.com/spreadsheets/d/1CzAo97L-ioGFHo_d8MC64nxAKiLchd-MkixYL4mxjwE 조 종사(Niq)

                    !rr 명령에 사용할 CoC 7th 시트를 등록하는 명령어입니다.
                    **"gid=???"** 부분이나 시트 이름이 반드시 포함되어야 합니다. gid뒤의 숫자부분은 시트페이지마다 다르며, 고유한 값입니다.
                    등록에 사용할 수 있는 서식은 https://stone-whale.postype.com/post/4912082의 기본 및 now 시트입니다.
                    등록한 시트는 서버 및 개인별로 별도로 저장됩니다."""),

    rr("CoC 7th 다이스 굴리기",
            """
                    !rr [기능/특성치 이름] (주사위 표현식)
                    !rr 근력
                    !rr 근접전(격투) +1d6

                    !ruse로 등록한 CoC 7th 시트의 주사위를 굴릴 수 있습니다.
                    공개된 CoC 7th 판정 기준을 따라 대성공/어려운성공/성공/실패/대실패를 판별합니다.
                    추가적인 주사위 표현식을 사용하여 판정에 보정을 적용 할 수 있습니다."""),

    rstat("등록된 CoC 7th 시트 확인", """
            !rstat\s

            !ruse 명령으로 현재 등록된 시트를 확인하는 기능입니다."""),

    rdel("등록된 CoC 7th 시트 해제",
            """
                    !rdel\s

                    !ruse 명령으로 등록한 시트를 해제하는 기능입니다."""),

    r("기본 주사위 굴림 명령어",
            """
                    !r [주사위 표현식]
                    !r 1d100+20
                    !r (1d20+4d20)>=50

                    [dice expr]에는 사칙연산 및 다이스 연산으로 표현된 주사위 표현식이 사용됩니다.
                    사용가능한 표현식은 아래와 같습니다.

                     - 사칙연산자(+,-,*,/), 비교연산자(<,>,<=,>=) 및 괄호
                     - NdR : N개의 R면체 주사위 굴리기
                     - Ndf : N개의 fudge(fate) 주사위 굴리기

                    각 연산사이에 사칙, 비교연산자를 사용하여 연결할 수 있으며, 비교 연산 후에는 성공/실패에 따라 값이 1/0으로 처리됩니다.
                    NdRhX 혹은 NdRlX 연산시에 d와 h 사이에 추가적인 연산을 하는 경우, 올바르지 않은 값이 출력될 수 있습니다."""),


    rw("단어 고르기 명령어",
            """
                    !rw [단어 1] [단어 2] [단어 3] ...
                    !rw 소고기 돼지고기 닭고기 양고기
                    
                    NdRhX 혹은 NdRlX 연산시에 d와 h 사이에 추가적인 연산을 하는 경우, 올바르지 않은 값이 출력될 수 있습니다."""),

    ra("주사위 표현식 별명 설정",
            """
                    !ra [별명] [주사위 표현식]
                    !ra crit 3d12+4
                    !ra atk 1d20+6<=

                    자주 사용하는 주사위 표현식에 별명을 지정할 수 있습니다.
                    별명이 지정된 표현식은 !rt 명령어를 통해 편리하게 굴릴 수 있습니다.
                    미완성 표현식에도 별명을 지정할 수 있습니다. 단, !rt 명령어에서 미완성된 표현식을 완성해야 합니다.
                    별명에는 띄어쓰기를 사용할 수 없습니다.
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
                    !rt [별명] (주사위 표현식)
                    !rt crit
                    !rt atk 16

                    !ra 명령어로 지정한 주사위 표헌식 별명을 굴립니다."""),

    rhelp("도움말 출력",
            """
                    !rhelp
                    !rhelp r
                    !rhelp ruse

                    사용가능한 명령어를 출력하거나, 각 명령어들의 상세 사용법을 출력합니다.""")
    ;

    static {

        BasicDice basicDice = new BasicDice();
        r.eventHandler = basicDice::command_r;
        rw.eventHandler = basicDice::command_rw;
        ra.eventHandler = basicDice::command_ra;
        rd.eventHandler = basicDice::command_rd;
        rl.eventHandler = basicDice::command_rl;
        rt.eventHandler = basicDice::command_rt;

        CoCDice cocDice = new CoCDice();
        ruse.eventHandler = cocDice::command_ruse;
        rr.eventHandler = cocDice::command_rr;
        rdel.eventHandler = cocDice::command_rdel;
        rstat.eventHandler = cocDice::command_rstat;
        rccc.eventHandler = cocDice::command_rccc;

        DnDDice dnDDice = new DnDDice();
        rdnd.eventHandler = dnDDice::command_rDnD;

        rhelp.eventHandler = event -> {
            String[] args = event.getMessage().getContentDisplay().split(" ");
            Commands[] commands = Commands.values();
            if(args.length<2){

                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("다이스 봇 명렁어");
                embed.setDescription("상세 도움말이 필요하면 **!rhelp [명령어]**를 입력하세요.");

                for (Commands command : commands) {
                    embed.addField(command.name(), command.simpleHelpMessage, false);
                }
                return embed.build();

            }else{
                //rhelp with parameter
                String target = args[1];
                for (Commands command : commands) {
                    if(command.name().equals(target)){
                        return SimpleEmbedMessage.titleDescEmbed(command.name()+" 명령어 사용법",
                                command.fullHelpMessage);
                    }
                }
                return SimpleEmbedMessage.titleDescEmbed(":x: 해당 명령어가 존재하지 않습니다.", "명령어를 확인해주세요.");
            }
        };

        executors = Executors.newCachedThreadPool();

    }


    public final String simpleHelpMessage;
    public final String fullHelpMessage;

    private Function<MessageReceivedEvent, MessageEmbed> eventHandler;
    private final static ExecutorService executors;

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

        try{
            executors.execute(() -> {
                try{
                    MessageEmbed result = eventHandler.apply(event);
                    log.info("[{}] [{}] {}: {} -> {}",
                            event.getGuild().getName(),
                            channel,
                            event.getMember().getEffectiveName(),
                            event.getMessage().getContentDisplay(),
                            result.getTitle()
                    );
                    event.getMessage().replyEmbeds(result).queue();
                }catch (Exception e){
                    log.info("[{}] [{}] {}: {} -> {}",
                            event.getGuild().getName(),
                            channel,
                            event.getMember().getEffectiveName(),
                            event.getMessage().getContentDisplay(),
                            "Execution Fail"
                    );
                    log.error("{}",e.getMessage(),e);
                    SimpleEmbedMessage.replyTitleDesc(event,":x: 명령을 실행하는데 실패했습니다.",
                            "!rhelp "+this.name()+" 명령어를 확인해주세요.");
                }

            });
        }catch (Exception e){
            System.out.println("testaaaa");
            log.error("{}",e.getMessage(),e);
            SimpleEmbedMessage.replyTitleDesc(event,":x: 명령을 실행하는데 실패했습니다.",
                    "!rhelp "+this.name()+" 명령어를 확인해주세요.");
        }

    }
}
