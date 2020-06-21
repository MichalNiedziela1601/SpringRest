import * as React from "react";
import Poll from "./Poll";
import PollCard from "./PollCard";


export default class Main extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            polls: [],
            isLoaded: false,
            totalPages: 1,
            currentPage: 1
        }
    }

    componentDidMount() {
        this.getPolls();
    }

    getPolls = ()=> {
        fetch(`${process.env.REACT_APP_API_HOST}/polls`)
            .then(res => res.json())
            .then(data => {
                this.setState({
                    isLoaded: true,
                    polls: data.content,
                    totalPages: data.totalPages,
                    size: data.size,
                    totalElements: data.totalElements
                })
            })
            .catch(console.log);
    };



    render() {
        if(!this.state.isLoaded || !this.state.polls ) return null;
        const elements = this.state.polls.map((poll)=>
            <PollCard pollId={poll.id} key={poll.id} title={poll.question}/>
        );
        return (
            <div>{elements}</div>
        )
    }
}